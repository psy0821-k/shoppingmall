package com.example.backend.wishlist.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WishlistRequest {
    private UUID userId;
    private UUID productId;
}
