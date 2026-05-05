package com.example.backend.payment.entity;

import com.example.backend.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, length = 20)
    private String status = "READY";

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "READY";
    }

    public void approve() {
        this.status = "PAID";
        this.paidAt = LocalDateTime.now();
        this.order.setStatus("PAID");
    }

    public void fail() {
        this.status = "FAILED";
        this.order.setStatus("PAYMENT_FAILED");
    }
}
