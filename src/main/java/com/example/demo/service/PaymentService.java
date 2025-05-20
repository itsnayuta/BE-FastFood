package com.example.demo.service;

import com.example.demo.entity.Bill;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface PaymentService {
    ResponseEntity<?> processPayment(Bill bill);
    boolean verifyPayment(Map<String, String> vnpParams);
} 