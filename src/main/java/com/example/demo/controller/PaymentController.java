package com.example.demo.controller;

import com.example.demo.entity.Bill;
import com.example.demo.service.PaymentService;
import com.example.demo.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillService billService;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody Bill bill) {
        String result = paymentService.processPayment(bill);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body("Không thể xử lý thanh toán");
    }

    @GetMapping("/vnpay/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> vnpParams) {
        boolean isValid = paymentService.verifyPayment(vnpParams);
        if (isValid) {
            String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
            String orderIdStr = vnpParams.get("vnp_TxnRef");
            Long orderId = Long.parseLong(orderIdStr);

            if ("00".equals(vnp_ResponseCode)) {
                // Tạo mới bill với trạng thái PAID
                Bill bill = new Bill();
                bill.setOrderId(orderId);
                bill.setPaymentStatus("PAID");
                // Có thể set thêm các thông tin khác nếu cần
                billService.createBill(bill); // Gọi hàm createBill để lưu vào DB
                return ResponseEntity.ok("Thanh toán thành công, bill đã được lưu vào DB");
            } else {
                return ResponseEntity.ok("Thanh toán thất bại");
            }
        }
        return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
    }
} 