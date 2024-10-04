package com.meta.reviewservice.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequest {
    private int rating;
    private long bookId;
    private long userId;
    private String comment;
}


