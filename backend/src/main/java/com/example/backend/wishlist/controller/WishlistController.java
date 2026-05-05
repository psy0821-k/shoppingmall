package com.example.backend.wishlist.controller;

import com.example.backend.wishlist.dto.WishlistRequest;
import com.example.backend.wishlist.dto.WishlistResponse;
import com.example.backend.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public List<WishlistResponse> getWishlists(@RequestParam UUID userId) {
        return wishlistService.getWishlists(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponse add(@RequestBody WishlistRequest request) {
        return wishlistService.add(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@RequestBody WishlistRequest request) {
        wishlistService.remove(request);
    }
}
