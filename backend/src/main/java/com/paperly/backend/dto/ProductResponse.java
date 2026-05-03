package com.paperly.backend.dto;

import com.paperly.backend.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int discountRate,
        int stock,
        String category,
        String imageUrl,
        String description
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountRate(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl(),
                product.getDescription()
        );
    }
}