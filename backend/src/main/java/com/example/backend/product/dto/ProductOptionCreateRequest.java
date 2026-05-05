package com.example.backend.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionCreateRequest {
    private String optionName;
    private Integer additionalPrice;
    private Integer stock;
}
