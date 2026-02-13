package net.catsnap.support.fixture;

import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewPhoto;

public class ReviewPhotoFixture {

    private Long id;
    private Review review = new ReviewFixture().build();
    private String photoFileName = "photoFileName";

    public ReviewPhotoFixture ReviewPhoto() {
        return new ReviewPhotoFixture();
    }

    public ReviewPhotoFixture Review(Review review) {
        this.review = review;
        return this;
    }

    public ReviewPhotoFixture photoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
        return this;
    }

    public ReviewPhoto build() {
        return ReviewPhoto.builder()
            .review(review)
            .photoFileName(photoFileName)
            .build();
    }
}
