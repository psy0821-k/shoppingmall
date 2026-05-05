package com.example.backend.wishlist.service;

import com.example.backend.product.entity.Product;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.wishlist.dto.WishlistRequest;
import com.example.backend.wishlist.dto.WishlistResponse;
import com.example.backend.wishlist.entity.Wishlist;
import com.example.backend.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<WishlistResponse> getWishlists(UUID userId) {
        User user = getUser(userId);
        return wishlistRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(WishlistResponse::from)
                .toList();
    }

    @Transactional
    public WishlistResponse add(WishlistRequest request) {
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());

        wishlistRepository.findByUserAndProduct(user, product).ifPresent(wishlist -> {
            throw new IllegalArgumentException("이미 찜한 상품입니다.");
        });

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        return WishlistResponse.from(wishlistRepository.save(wishlist));
    }

    @Transactional
    public void remove(WishlistRequest request) {
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());
        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalArgumentException("찜한 상품이 아닙니다."));
        wishlistRepository.delete(wishlist);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
