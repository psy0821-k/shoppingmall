package com.example.backend.review.repository;

import com.example.backend.product.entity.Product;
import com.example.backend.review.entity.Review;
import com.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByProductOrderByCreatedAtDesc(Product product);
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    Optional<Review> findByUserAndProduct(User user, Product product);
}
