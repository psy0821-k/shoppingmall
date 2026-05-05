package com.example.backend.product.service;

import com.example.backend.category.entity.Category;
import com.example.backend.category.repository.CategoryRepository;
import com.example.backend.product.dto.*;
import com.example.backend.product.entity.Product;
import com.example.backend.product.entity.ProductOption;
import com.example.backend.product.repository.ProductOptionRepository;
import com.example.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse getProduct(UUID productId) {
        Product product = getProductEntity(productId);
        return ProductResponse.from(product);
    }

    public List<ProductOptionResponse> getOptions(UUID productId) {
        return productOptionRepository.findByProductId(productId).stream()
                .map(ProductOptionResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Product product = Product.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .discountRate(request.getDiscountRate() == null ? 0 : request.getDiscountRate())
                .thumbnailUrl(request.getThumbnailUrl())
                .status(request.getStatus() == null ? "ACTIVE" : request.getStatus())
                .build();

        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductOptionResponse createOption(UUID productId, ProductOptionCreateRequest request) {
        Product product = getProductEntity(productId);
        ProductOption option = ProductOption.builder()
                .product(product)
                .optionName(request.getOptionName())
                .additionalPrice(request.getAdditionalPrice() == null ? 0 : request.getAdditionalPrice())
                .stock(request.getStock() == null ? 0 : request.getStock())
                .build();
        return ProductOptionResponse.from(productOptionRepository.save(option));
    }

    private Product getProductEntity(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
