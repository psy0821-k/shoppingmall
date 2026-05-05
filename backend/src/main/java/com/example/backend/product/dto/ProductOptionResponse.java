package com.example.backend.product.dto;

import com.example.backend.product.entity.ProductOption;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductOptionResponse {
    private UUID id;
    private UUID productId;
    private String optionName;
    private Integer additionalPrice;
    private Integer stock;

    public static ProductOptionResponse from(ProductOption option) {
        return ProductOptionResponse.builder()
                .id(option.getId())
                .productId(option.getProduct().getId())
                .optionName(option.getOptionName())
                .additionalPrice(option.getAdditionalPrice())
                .stock(option.getStock())
                .build();
    }
}
