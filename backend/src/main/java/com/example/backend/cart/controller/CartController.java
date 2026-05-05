package com.example.backend.cart.controller;

import com.example.backend.cart.dto.*;
import com.example.backend.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(@RequestParam UUID userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@RequestBody CartAddRequest request) {
        return cartService.addItem(request);
    }

    @PatchMapping("/items/{cartItemId}")
    public CartResponse updateQuantity(@RequestParam UUID userId,
                                       @PathVariable UUID cartItemId,
                                       @RequestBody CartQuantityUpdateRequest request) {
        return cartService.updateQuantity(userId, cartItemId, request);
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestParam UUID userId, @PathVariable UUID cartItemId) {
        cartService.deleteItem(userId, cartItemId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestParam UUID userId) {
        cartService.clearCart(userId);
    }
}
