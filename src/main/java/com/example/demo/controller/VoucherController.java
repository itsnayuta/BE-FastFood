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
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        return ResponseEntity.ok(voucherService.createVoucher(voucher));
    }

    // API cập nhật voucher theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher updatedVoucher) {
        Voucher voucher = voucherService.updateVoucher(id, updatedVoucher);
        return (voucher != null) ? ResponseEntity.ok(voucher) : ResponseEntity.notFound().build();
    }

    // API xóa voucher theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        boolean deleted = voucherService.deleteVoucher(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
