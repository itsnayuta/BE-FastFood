package com.example.demo.service.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.enums.PaymentStatus;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public Bill createBill(Bill bill) {
        bill.setPaymentStatus(PaymentStatus.PENDING);
        return billRepository.save(bill);
    }

    @Override
    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public List<Bill> getBillsByMemberId(Long memberId) {
        return billRepository.findByMemberId(memberId);
    }

    @Override
    public List<Bill> getBillsByOrderId(Long orderId) {
        return billRepository.findByMemberId(orderId);
    }

    @Override
    public List<Bill> getBillsByPaymentStatus(PaymentStatus status) {
        return billRepository.findByPaymentStatus(status);
    }

    @Override
    public Bill updateBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    @Override
    public Bill updatePaymentStatus(Long billId, PaymentStatus status) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setPaymentStatus(status);
        return billRepository.save(bill);
    }
} 