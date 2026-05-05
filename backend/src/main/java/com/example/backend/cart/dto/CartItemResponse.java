package com.example.backend.cart.dto;

import com.example.backend.cart.entity.CartItem;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CartItemResponse {
    private UUID cartItemId;
    private UUID productId;
    private UUID productOptionId;
    private String productName;
    private String optionName;
    private String thumbnailUrl;
    private Integer unitPrice;
    private Integer quantity;
    private Integer totalPrice;

    public static CartItemResponse from(CartItem item) {
        int unitPrice = item.getProductOption().getSalePrice();
        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .productId(item.getProductOption().getProduct().getId())
                .productOptionId(item.getProductOption().getId())
                .productName(item.getProductOption().getProduct().getName())
                .optionName(item.getProductOption().getOptionName())
                .thumbnailUrl(item.getProductOption().getProduct().getThumbnailUrl())
                .unitPrice(unitPrice)
                .quantity(item.getQuantity())
                .totalPrice(unitPrice * item.getQuantity())
                .build();
    }
}
