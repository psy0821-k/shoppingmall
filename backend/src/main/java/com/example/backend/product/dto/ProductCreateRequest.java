package com.example.backend.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductCreateRequest {
    private UUID categoryId;
    private String name;
    private String description;
    private Integer price;
    private Integer discountRate;
    private String thumbnailUrl;
    private String status;
}
