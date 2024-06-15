package com.meta.reviewservice.service;

import com.meta.reviewservice.entity.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Review review);
    void deleteReview(Long reviewId);
    Review updateReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long reviewId);
    List<Review> getReviewsByUserId(Long userId);
}
