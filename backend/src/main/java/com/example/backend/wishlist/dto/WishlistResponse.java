package com.example.backend.wishlist.dto;

import com.example.backend.wishlist.entity.Wishlist;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class WishlistResponse {
    private UUID wishlistId;
    private UUID productId;
    private String productName;
    private String thumbnailUrl;
    private Integer price;
    private Integer discountedPrice;

    public static WishlistResponse from(Wishlist wishlist) {
        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .productId(wishlist.getProduct().getId())
                .productName(wishlist.getProduct().getName())
                .thumbnailUrl(wishlist.getProduct().getThumbnailUrl())
                .price(wishlist.getProduct().getPrice())
                .discountedPrice(wishlist.getProduct().getDiscountedPrice())
                .build();
    }
}
