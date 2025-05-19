package com.example.demo.service.impl;

import com.example.demo.entity.Bill;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> getBillById(Long orderId) {
        return billRepository.findById(orderId);
    }

    @Override
    public List<Bill> getBillsByMemberId(Long memberId) {
        return billRepository.findByMemberId(memberId);
    }

    @Override
    public List<Bill> getBillsByPaymentStatus(String paymentStatus) {
        return billRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Long orderId, Bill updatedBill) {
        return billRepository.findById(orderId)
                .map(existingBill -> {
                    existingBill.setMemberId(updatedBill.getMemberId());
                    existingBill.setPaymentMethod(updatedBill.getPaymentMethod());
                    existingBill.setDiscount(updatedBill.getDiscount());
                    existingBill.setTotalPayment(updatedBill.getTotalPayment());
                    existingBill.setTax(updatedBill.getTax());
                    existingBill.setPaymentStatus(updatedBill.getPaymentStatus());
                    existingBill.setVoucher(updatedBill.getVoucher());
                    return billRepository.save(existingBill);
                })
                .orElse(null);
    }

    @Override
    public boolean deleteBill(Long orderId) {
        if (billRepository.existsById(orderId)) {
            billRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
} 