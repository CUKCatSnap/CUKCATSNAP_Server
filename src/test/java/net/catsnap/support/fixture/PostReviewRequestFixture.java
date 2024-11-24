package net.catsnap.support.fixture;

import net.catsnap.domain.review.dto.request.PostReviewRequest;
import java.util.List;

public class PostReviewRequestFixture {

    private Long reservationId = 1L;
    private Integer placeScore = 3;
    private Integer photographerScore = 3;
    private String content = "대충 좋은 후기";
    private List<String> photoFileNameList = List.of("photo1.jpg", "photo2.jpg");


    public static PostReviewRequestFixture postReviewRequest() {
        return new PostReviewRequestFixture();
    }

    public PostReviewRequest build() {
        return new PostReviewRequest(reservationId, placeScore, photographerScore, content,
            photoFileNameList);
    }
}
