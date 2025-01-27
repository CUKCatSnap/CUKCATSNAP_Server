package net.catsnap.support.fixture;

import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.user.entity.User;

public class ReviewLikeFixture {

    private Long id;
    private Review review = ReviewFixture.review().build();
    private User user;

    public static ReviewLikeFixture reviewLike() {
        return new ReviewLikeFixture();
    }

    public ReviewLikeFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ReviewLikeFixture review(Review review) {
        this.review = review;
        return this;
    }

    public ReviewLikeFixture user(User user) {
        this.user = user;
        return this;
    }

    public ReviewLike build() {
        return ReviewLike.builder()
            .id(id)
            .review(review)
            .user(user)
            .build();
    }
}
