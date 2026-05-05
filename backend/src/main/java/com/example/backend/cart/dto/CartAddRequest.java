package com.example.backend.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartAddRequest {
    private UUID userId;
    private UUID productOptionId;
    private Integer quantity;
}
