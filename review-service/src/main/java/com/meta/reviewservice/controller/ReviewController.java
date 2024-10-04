package com.meta.reviewservice.controller;

import com.meta.reviewservice.client.BookServiceClient;
import com.meta.reviewservice.converter.ReviewMapper;
import com.meta.reviewservice.dto.BookDto;
import com.meta.reviewservice.dto.ReviewDto;
import com.meta.reviewservice.exception.EntityNotFoundException;
import com.meta.reviewservice.payload.ReviewRequest;
import com.meta.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewController {
    private ReviewService reviewService;
    private ReviewMapper reviewMapper;
    private BookServiceClient bookServiceClient;
    @Autowired
    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper, BookServiceClient bookServiceClient) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.bookServiceClient = bookServiceClient;
    }

    // 1. Add a method to return all reviews
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviews() {
        log.info("process=get-reviews");
        return reviewMapper.toDto(reviewService.getAllReviews());
    }
    // 2. Add a method to create a new review
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        log.info("process=create-review, review={}", reviewRequest);
        return reviewMapper.toDto(reviewService.addReview(reviewMapper.toEntity(reviewRequest)));
    }
    // 3. Add a method to return a review by its ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReviewById(@PathVariable Long id) {
        log.info("process=get-review, review_id={}", id);
        return Optional.ofNullable(reviewService.getReviewById(id))
                .map(review -> reviewMapper.toDto(review))
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }
    // 4. Add a method to update a review by its ID
    @PutMapping("/{id}")
    public ReviewDto updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewdto) {
        log.info("process=update-review, review_id={}", id);
        Optional.ofNullable(reviewService.getReviewById(id))
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        return reviewMapper.toDto(reviewService.updateReview(reviewMapper.toEntity(reviewdto)));
    }
    // 5. Add a method to delete a review by its ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        log.info("process=delete-review, review_id={}", id);
        Optional.ofNullable(reviewService.getReviewById(id))
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    // 6. Add a method to return all reviews by user ID
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviewsByUserId(@PathVariable Long userId) {
        log.info("process=get-reviews-by-user-id, user_id={}", userId);
        return reviewMapper.toDto(reviewService.getReviewsByUserId(userId));
    }

    // 7. Add a method to return all reviews by book ID
    @GetMapping("/book/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getReviewsByBookId(@PathVariable Long bookId) {
        log.info("process=get-reviews-by-book-id, book_id={}", bookId);
        BookDto bookDto = Optional.ofNullable(this.bookServiceClient.getBookById(bookId))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        return reviewService.getReviewsByBookId(bookId).stream().map(reviewMapper::toDto).toList();
    }
}
