package com.example.backend.product.controller;

import com.example.backend.product.dto.*;
import com.example.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping("/{productId}/options")
    public List<ProductOptionResponse> getOptions(@PathVariable UUID productId) {
        return productService.getOptions(productId);
    }

    @PostMapping("/{productId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductOptionResponse createOption(@PathVariable UUID productId,
                                              @RequestBody ProductOptionCreateRequest request) {
        return productService.createOption(productId, request);
    }
}
