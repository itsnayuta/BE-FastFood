package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 255)
    private String paymentMethod;

    private Float discount;

    @Column(nullable = false)
    private Float totalPayment;

    private Float tax;

    @Column(length = 50)
    private String paymentStatus;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @PreUpdate
    protected void onUpdate() {
        createdAt = LocalDateTime.now();
    }
} 