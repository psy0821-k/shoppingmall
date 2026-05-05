package com.example.backend.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderCreateRequest {
    private UUID userId;
    private Integer deliveryFee;
}
