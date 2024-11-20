package com.cuk.catsnap.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PostReviewResponse(
    @Schema(description = "리뷰를 남기고자 하는 예약의 Id")
    Long reservationId,
    @Schema(description = "장소에 대한 점수. 1점부터 5점까지 가능. 정수로만 가능")
    Integer placeScore,
    @Schema(description = "작가에 대한 점수. 1점부터 5점까지 가능. 정수로만 가능")
    Integer photographerScore,
    @Schema(description = "리뷰 내용")
    String content,
    @Schema(description = "업로드 하고자 하는 사진 파일의 이름")
    List<String> photoFileNameList
) {

}
