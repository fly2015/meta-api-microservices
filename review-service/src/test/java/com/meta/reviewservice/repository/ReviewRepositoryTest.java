package com.meta.reviewservice.repository;

import com.meta.reviewservice.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void testFindByUserId() {
        // given
        Review review = new Review();
        review.setUserId(1);
        review.setComment("Great product!");
        entityManager.persistAndFlush(review);

        // when
        List<Review> foundReviews = reviewRepository.findByUserId(1L);

        // then
        assertThat(foundReviews).isNotEmpty();
        assertThat(foundReviews.get(0).getComment()).isEqualTo(review.getComment());
    }
}
