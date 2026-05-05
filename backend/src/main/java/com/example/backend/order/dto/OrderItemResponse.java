package com.example.backend.order.dto;

import com.example.backend.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderItemResponse {
    private UUID orderItemId;
    private UUID productId;
    private UUID productOptionId;
    private String productName;
    private String optionName;
    private Integer quantity;
    private Integer price;
    private Integer totalPrice;

    public static OrderItemResponse from(OrderItem item) {
        return OrderItemResponse.builder()
                .orderItemId(item.getId())
                .productId(item.getProductOption().getProduct().getId())
                .productOptionId(item.getProductOption().getId())
                .productName(item.getProductOption().getProduct().getName())
                .optionName(item.getProductOption().getOptionName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getPrice() * item.getQuantity())
                .build();
    }
}
