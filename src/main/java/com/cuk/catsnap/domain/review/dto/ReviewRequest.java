package com.cuk.catsnap.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequest {

    /*
     *새로운 리뷰를 만들기 위한 형식입니다.
     */
    @Getter
    @NoArgsConstructor
    public static class PostReview {

        @Schema(description = "리뷰를 남기고자 하는 예약의 Id")
        private Long reservationId;
        @Schema(description = "장소에 대한 점수. 1점부터 5점까지 가능. 정수로만 가능")
        private Integer placeRating;
        @Schema(description = "작가에 대한 점수. 1점부터 5점까지 가능. 정수로만 가능")
        private Integer photographerRating;
        @Schema(description = "리뷰 내용")
        private String content;
        @Schema(description = "업로드 하고자 하는 사진 파일의 이름")
        private List<String> photoFileNameList;
    }
}
