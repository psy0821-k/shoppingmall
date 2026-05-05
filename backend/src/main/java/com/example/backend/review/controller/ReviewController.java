package com.example.backend.review.controller;

import com.example.backend.review.dto.ReviewCreateRequest;
import com.example.backend.review.dto.ReviewResponse;
import com.example.backend.review.dto.ReviewUpdateRequest;
import com.example.backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/products/{productId}")
    public List<ReviewResponse> getProductReviews(@PathVariable UUID productId) {
        return reviewService.getProductReviews(productId);
    }

    @GetMapping("/users/{userId}")
    public List<ReviewResponse> getUserReviews(@PathVariable UUID userId) {
        return reviewService.getUserReviews(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@RequestBody ReviewCreateRequest request) {
        return reviewService.create(request);
    }

    @PatchMapping("/{reviewId}")
    public ReviewResponse update(@PathVariable UUID reviewId,
                                 @RequestParam UUID userId,
                                 @RequestBody ReviewUpdateRequest request) {
        return reviewService.update(reviewId, userId, request);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID reviewId, @RequestParam UUID userId) {
        reviewService.delete(reviewId, userId);
    }
}
