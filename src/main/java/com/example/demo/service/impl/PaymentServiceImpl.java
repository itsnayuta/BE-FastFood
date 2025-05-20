package com.example.demo.service.impl;

import com.example.demo.config.VNPayConfig;
import com.example.demo.entity.Bill;
import com.example.demo.entity.enums.PaymentMethod;
import com.example.demo.entity.enums.PaymentStatus;
import com.example.demo.service.PaymentService;
import com.example.demo.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private BillService billService;

    @Override
    public ResponseEntity<?> processPayment(Bill bill) {
        if (PaymentMethod.CASH.equals(bill.getPaymentMethod())) {
            bill.setPaymentStatus(PaymentStatus.PENDING);
            Bill savedBill = billService.createBill(bill);
            return ResponseEntity.ok("Payment pending - Cash on delivery");
        } else if (PaymentMethod.VNPAY.equals(bill.getPaymentMethod())) {
            bill.setPaymentStatus(PaymentStatus.PENDING);
            Bill savedBill = billService.createBill(bill);
            String paymentUrl = createVNPayUrl(savedBill);
            if (paymentUrl != null) {
                return ResponseEntity.ok(paymentUrl);
            }
            return ResponseEntity.badRequest().body("Failed to create payment URL");
        }
        return ResponseEntity.badRequest().body("Invalid payment method");
    }

    @Override
    public boolean verifyPayment(Map<String, String> vnpParams) {
        try {
            String vnp_SecureHash = vnpParams.remove("vnp_SecureHash");
            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                        hashData.append('&');
                    }
                }
            }

            String calculatedHash = hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
            return calculatedHash.equals(vnp_SecureHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createVNPayUrl(Bill bill) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = "Thanh toan don hang " + bill.getId();
            String vnp_OrderType = "other";
            String vnp_Locale = "vn";
            String vnp_CurrCode = "VND";
            
            // Generate unique transaction reference by combining bill ID and timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String vnp_TxnRef = bill.getId() + "_" + timestamp;
            
            String vnp_IpAddr = "127.0.0.1";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnPayConfig.getVnp_TmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(Math.round(bill.getTotalPayment() * 100)));
            vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
            vnp_Params.put("vnp_BankCode", "");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", vnp_OrderType);
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Log URL trả về
            logger.info("Return URL: {}", vnPayConfig.getVnp_ReturnUrl());

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            String finalUrl = vnPayConfig.getVnp_Url() + "?" + queryUrl;
            logger.info("Final payment URL: {}", finalUrl);
            return finalUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] hash = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
} 