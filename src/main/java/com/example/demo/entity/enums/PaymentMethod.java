package com.example.demo.entity.enums;

public enum PaymentMethod {
    CASH("Thanh toán tiền mặt"),
    VNPAY("Thanh toán qua VNPay");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 