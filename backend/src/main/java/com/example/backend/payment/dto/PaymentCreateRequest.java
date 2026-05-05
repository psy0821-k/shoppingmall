package com.example.backend.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentCreateRequest {
    private UUID orderId;
    private String paymentMethod;
}
