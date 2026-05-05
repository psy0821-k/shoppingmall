package com.example.backend.review.service;

import com.example.backend.product.entity.Product;
import com.example.backend.product.repository.ProductRepository;
import com.example.backend.review.dto.ReviewCreateRequest;
import com.example.backend.review.dto.ReviewResponse;
import com.example.backend.review.dto.ReviewUpdateRequest;
import com.example.backend.review.entity.Review;
import com.example.backend.review.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<ReviewResponse> getProductReviews(UUID productId) {
        Product product = getProduct(productId);
        return reviewRepository.findByProductOrderByCreatedAtDesc(product).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    public List<ReviewResponse> getUserReviews(UUID userId) {
        User user = getUser(userId);
        return reviewRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse create(ReviewCreateRequest request) {
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());

        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("평점은 1점에서 5점 사이여야 합니다.");
        }

        reviewRepository.findByUserAndProduct(user, product).ifPresent(review -> {
            throw new IllegalArgumentException("이미 리뷰를 작성한 상품입니다.");
        });

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .content(request.getContent())
                .build();
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public ReviewResponse update(UUID reviewId, UUID userId, ReviewUpdateRequest request) {
        Review review = getReview(reviewId);
        validateOwner(review, userId);
        review.update(request.getRating(), request.getContent());
        return ReviewResponse.from(review);
    }

    @Transactional
    public void delete(UUID reviewId, UUID userId) {
        Review review = getReview(reviewId);
        validateOwner(review, userId);
        reviewRepository.delete(review);
    }

    private Review getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    private void validateOwner(Review review, UUID userId) {
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 리뷰를 수정/삭제할 수 없습니다.");
        }
    }
}
