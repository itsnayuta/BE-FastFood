package com.example.demo.controller;

import com.example.demo.entity.Voucher;
import com.example.demo.service.VoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin(origins = "*")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // API lấy danh sách tất cả voucher
    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    // API lấy voucher theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Voucher>> getVoucherById(@PathVariable Long id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return (voucher != null) ? ResponseEntity.ok(voucher) : ResponseEntity.notFound().build();
    }

    // API tạo mới voucher
    @PostMapping
    public ResponseEntity<?> createVoucher(@RequestBody Voucher voucher) {
        try {
            if (voucher.getCode() == null || voucher.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Code is required");
            }
            if (voucher.getDiscount() == null) {
                return ResponseEntity.badRequest().body("Discount is required");
            }
            
            Voucher createdVoucher = voucherService.createVoucher(voucher);
            return ResponseEntity.ok(createdVoucher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating voucher: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable Long id, @RequestBody Voucher updatedVoucher) {
        try {
            if (updatedVoucher.getCode() == null || updatedVoucher.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Code is required");
            }
            if (updatedVoucher.getDiscount() == null) {
                return ResponseEntity.badRequest().body("Discount is required");
            }

            Voucher voucher = voucherService.updateVoucher(id, updatedVoucher);
            return (voucher != null) ? ResponseEntity.ok(voucher) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating voucher: " + e.getMessage());
        }
    }

    // API xóa voucher theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        boolean deleted = voucherService.deleteVoucher(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
