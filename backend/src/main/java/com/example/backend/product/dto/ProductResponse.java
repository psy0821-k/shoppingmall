package com.example.backend.product.dto;

import com.example.backend.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductResponse {
    private UUID id;
    private UUID categoryId;
    private String name;
    private String description;
    private Integer price;
    private Integer discountRate;
    private Integer discountedPrice;
    private String thumbnailUrl;
    private String status;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountRate(product.getDiscountRate())
                .discountedPrice(product.getDiscountedPrice())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(product.getStatus())
                .build();
    }
}
