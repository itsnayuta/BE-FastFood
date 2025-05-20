package com.example.demo.controller;

import com.example.demo.entity.Bill;
import com.example.demo.entity.enums.PaymentMethod;
import com.example.demo.entity.enums.PaymentStatus;
import com.example.demo.service.BillService;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillService billService;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody Bill bill) {
        // Đảm bảo trạng thái thanh toán là PENDING khi tạo mới
        bill.setPaymentStatus(PaymentStatus.PENDING);
        
        // Nếu không có discount, tax thì set về 0
        if (bill.getDiscount() == null) {
            bill.setDiscount(0.0);
        }
        if (bill.getTax() == null) {
            bill.setTax(0.0);
        }
        
        // Đảm bảo totalPayment đã được set
        if (bill.getTotalPayment() == null) {
            return ResponseEntity.badRequest().body("Total payment is required");
        }

        return paymentService.processPayment(bill);
    }

    @GetMapping("/vnpay/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> vnpParams) {
        try {
            String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
            String vnp_TxnRef = vnpParams.get("vnp_TxnRef");
            
            // Kiểm tra mã phản hồi từ VNPAY
            if ("00".equals(vnp_ResponseCode)) {
                String billId = vnp_TxnRef.split("_")[0];
                Bill bill = billService.getBillById(Long.parseLong(billId))
                        .orElseThrow(() -> new RuntimeException("Bill not found"));
                
                bill.setPaymentStatus(PaymentStatus.COMPLETED);
                billService.updateBill(bill);
                
                return ResponseEntity.ok(Map.of(
                    "billId", billId,
                    "status", "success"
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "billId", vnp_TxnRef != null ? vnp_TxnRef.split("_")[0] : "unknown",
                    "status", "error"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "status", "error"
            ));
        }
    }

    @PutMapping("/cod/{billId}/complete")
    public ResponseEntity<?> completeCodPayment(@PathVariable Long billId) {
        try {
            logger.info("Processing COD payment completion for bill ID: {}", billId);
            
            Bill bill = billService.getBillById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            
            if (bill.getPaymentMethod() != PaymentMethod.CASH) {
                logger.warn("Bill {} is not a COD payment", billId);
                return ResponseEntity.badRequest().body("This bill is not a COD payment");
            }

            // Log trạng thái trước khi cập nhật
            logger.info("Current payment status: {}", bill.getPaymentStatus());
            
            bill.setPaymentStatus(PaymentStatus.COMPLETED);
            Bill updatedBill = billService.updateBill(bill);
            
            // Log trạng thái sau khi cập nhật
            logger.info("Updated payment status: {}", updatedBill.getPaymentStatus());
            
            return ResponseEntity.ok(updatedBill);
        } catch (Exception e) {
            logger.error("Error completing COD payment: ", e);
            return ResponseEntity.badRequest().body("Error updating payment status: " + e.getMessage());
        }
    }
} 