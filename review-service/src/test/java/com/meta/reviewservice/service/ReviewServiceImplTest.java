package com.meta.reviewservice.service;

import com.meta.reviewservice.entity.Review;
import com.meta.reviewservice.repository.ReviewRepository;
import com.meta.reviewservice.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReviewServiceImplTest {
    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddReview() {
        Review review = new Review();
        review.setReviewId(Integer.valueOf(1));
        review.setUserId(Integer.valueOf(1));
        review.setComment("Great product!");

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review created = reviewService.addReview(review);

        assertEquals(created.getComment(), review.getComment());
        verify(reviewRepository, times(1)).save(review);
    }
}
