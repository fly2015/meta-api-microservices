package com.meta.reviewservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Integer book;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;
}

