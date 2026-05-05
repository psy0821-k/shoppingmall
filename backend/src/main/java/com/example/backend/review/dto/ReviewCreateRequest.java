package com.example.backend.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewCreateRequest {
    private UUID userId;
    private UUID productId;
    private Integer rating;
    private String content;
}
