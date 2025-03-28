package com.example.demo.service;

import com.example.demo.entity.Voucher;
import com.example.demo.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Long id) {
        return voucherRepository.findById(id);
    }

    public Optional<Voucher> getVoucherByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Long id, Voucher updatedVoucher) {
        return voucherRepository.findById(id).map(voucher -> {
            voucher.setCode(updatedVoucher.getCode());
            voucher.setDescription(updatedVoucher.getDescription());
            voucher.setDiscount(updatedVoucher.getDiscount());
            voucher.setMaxDiscount(updatedVoucher.getMaxDiscount()); 
            voucher.setMinOrderAmount(updatedVoucher.getMinOrderAmount()); 
            voucher.setPaymentMethod(updatedVoucher.getPaymentMethod());
            voucher.setStartDate(updatedVoucher.getStartDate()); 
            voucher.setEndDate(updatedVoucher.getEndDate()); 
            voucher.setUsageLimit(updatedVoucher.getUsageLimit());
            return voucherRepository.save(voucher);
        }).orElse(null);
    }

    public boolean deleteVoucher(Long id) {
        if (voucherRepository.existsById(id)) {
            voucherRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
