package com.cuk.catsnap.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

    @Getter
    @Builder
    public static class ReviewPhotoPresignedURL {
        @Schema(description = "새로 만들어진 리뷰의 Id")
        private Long reviewId;
        @Schema(description = "사진이 AWS s3 저장될 이름")
        private String photoName;
        @Schema(description = "AWS s3 presignedURL")
        private String presignedURL;
        @Schema(description = "사진 업로드 완료 후, 사진을 접근할 수 있는 URL")
        private String photoURL;
    }
}
