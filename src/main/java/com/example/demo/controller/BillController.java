package com.example.demo.controller;

import com.example.demo.entity.Bill;
import com.example.demo.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long orderId) {
        Optional<Bill> bill = billService.getBillById(orderId);
        return bill.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Bill>> getBillsByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(billService.getBillsByMemberId(memberId));
    }

    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<Bill>> getBillsByPaymentStatus(@PathVariable String paymentStatus) {
        return ResponseEntity.ok(billService.getBillsByPaymentStatus(paymentStatus));
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return ResponseEntity.ok(billService.createBill(bill));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long orderId, @RequestBody Bill updatedBill) {
        Bill bill = billService.updateBill(orderId, updatedBill);
        return (bill != null) ? ResponseEntity.ok(bill) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long orderId) {
        boolean deleted = billService.deleteBill(orderId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
} 