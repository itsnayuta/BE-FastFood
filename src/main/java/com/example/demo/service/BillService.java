package com.example.demo.service;

import com.example.demo.entity.Bill;
import com.example.demo.entity.enums.PaymentStatus;
import java.util.List;
import java.util.Optional;

public interface BillService {
    Bill createBill(Bill bill);
    Optional<Bill> getBillById(Long id);
    List<Bill> getBillsByMemberId(Long memberId);
    List<Bill> getBillsByOrderId(Long orderId);
    List<Bill> getBillsByPaymentStatus(PaymentStatus status);
    Bill updateBill(Bill bill);
    void deleteBill(Long id);
    Bill updatePaymentStatus(Long billId, PaymentStatus status);
} 