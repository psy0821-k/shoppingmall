package com.example.backend.cart.service;

import com.example.backend.cart.dto.*;
import com.example.backend.cart.entity.Cart;
import com.example.backend.cart.entity.CartItem;
import com.example.backend.cart.repository.CartItemRepository;
import com.example.backend.cart.repository.CartRepository;
import com.example.backend.product.entity.ProductOption;
import com.example.backend.product.repository.ProductOptionRepository;
import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;

    public CartResponse getCart(UUID userId) {
        User user = getUser(userId);
        Cart cart = getOrCreateCart(user);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse addItem(CartAddRequest request) {
        User user = getUser(request.getUserId());
        Cart cart = getOrCreateCart(user);
        ProductOption option = getProductOption(request.getProductOptionId());
        int quantity = request.getQuantity() == null ? 1 : request.getQuantity();

        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        if (option.getStock() < quantity) throw new IllegalArgumentException("재고가 부족합니다.");

        CartItem item = cartItemRepository.findByCartAndProductOption(cart, option)
                .map(existing -> {
                    existing.addQuantity(quantity);
                    return existing;
                })
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .productOption(option)
                        .quantity(quantity)
                        .build());

        cartItemRepository.save(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse updateQuantity(UUID userId, UUID cartItemId, CartQuantityUpdateRequest request) {
        CartItem item = getCartItem(cartItemId);
        validateCartOwner(item, userId);
        item.changeQuantity(request.getQuantity());
        return toResponse(item.getCart());
    }

    @Transactional
    public void deleteItem(UUID userId, UUID cartItemId) {
        CartItem item = getCartItem(cartItemId);
        validateCartOwner(item, userId);
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(UUID userId) {
        User user = getUser(userId);
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cartItemRepository.findByCart(cart).stream()
                .map(CartItemResponse::from)
                .toList();
        int totalPrice = items.stream().mapToInt(CartItemResponse::getTotalPrice).sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalPrice(totalPrice)
                .build();
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private ProductOption getProductOption(UUID optionId) {
        return productOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("상품 옵션을 찾을 수 없습니다."));
    }

    private CartItem getCartItem(UUID cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));
    }

    private void validateCartOwner(CartItem item, UUID userId) {
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 장바구니 상품에 접근할 수 없습니다.");
        }
    }
}
