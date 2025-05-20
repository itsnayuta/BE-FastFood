package com.example.demo.service;

import com.example.demo.entity.Bill;
import java.util.Map;

public interface PaymentService {
    String processPayment(Bill bill);
    boolean verifyPayment(Map<String, String> vnpParams);
    void updatePaymentStatus(Bill bill, String status);
} 