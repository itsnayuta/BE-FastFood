package com.example.demo.service;

import com.example.demo.entity.Bill;
import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> getAllBills();
    Optional<Bill> getBillById(Long orderId);
    List<Bill> getBillsByMemberId(Long memberId);
    List<Bill> getBillsByPaymentStatus(String paymentStatus);
    Bill createBill(Bill bill);
    Bill updateBill(Long orderId, Bill updatedBill);
    boolean deleteBill(Long orderId);
} 