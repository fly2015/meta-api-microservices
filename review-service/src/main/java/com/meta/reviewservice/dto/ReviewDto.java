package com.meta.reviewservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
    private long reviewId;
    private int rating;
    private long bookId;
    private long userId;
    private String comment;
}
