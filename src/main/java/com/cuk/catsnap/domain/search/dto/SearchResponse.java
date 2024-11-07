package com.cuk.catsnap.domain.search.dto;

import com.cuk.catsnap.domain.member.dto.MemberResponse;
import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class SearchResponse {

    @Getter
    @Builder
    public static class PreviewFeedSearchList {

        private List<PreviewFeedSearch> previewFeedSearchList;
    }

    @Getter
    @Builder
    public static class PreviewFeedSearch {

        private PhotographerResponse.PhotographerFullyInformation photographerFullyInformation;

        @Schema(description = "검색된 피드의 id")
        private Long feedId;

        @Schema(description = "위치 정보")
        private ReservationResponse.Location location;

        @Schema(description = "검색에 보여줄 이미지 1장")
        private String firstPhotoUrl;

        @Schema(description = "글 작성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class DetailFeedSearchList {

        private List<DetailFeedSearch> detailFeedSearchList;
    }

    @Getter
    @Builder
    public static class DetailFeedSearch {

        private PhotographerResponse.PhotographerFullyInformation photographerFullyInformation;

        @Schema(description = "글 작성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @Schema(description = "위치 정보")
        private ReservationResponse.Location location;

        @Schema(description = "피드의 내용")
        private String content;

        @Schema(description = "피드 사진 URL")
        private List<String> photoURLList;

        @Schema(description = "좋아요 수")
        private Long likeCount;

        @Schema(description = "내가 좋아요를 눌렀는지")
        private Boolean isMeLiked;
    }

    @Getter
    @Builder
    public static class PreviewReviewSearchList {

        private List<PreviewReviewSearch> previewReviewSearchList;
    }

    @Getter
    @Builder
    public static class PreviewReviewSearch {

        private PhotographerResponse.PhotographerFullyInformation photographerFullyInformation;
        private MemberResponse.MemberTinyInformation memberFullyInformation;

        @Schema(description = "검색된 리뷰의 id")
        private Long reviewId;

        @Schema(description = "위치 정보")
        private ReservationResponse.Location location;

        @Schema(description = "검색에 보여줄 이미지 1장")
        private String firstPhotoUrl;

        @Schema(description = "작가에 대한 별점")
        private Integer photographerRating;

        @Schema(description = "장소에 대한 별점")
        private Integer placeRating;

        @Schema(description = "글 작성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class DetailReviewSearchList {

        List<DetailReviewSearch> detailReviewSearchList;
    }

    @Getter
    @Builder
    public static class DetailReviewSearch {

        private PhotographerResponse.PhotographerFullyInformation photographerFullyInformation;

        @Schema(description = "글 작성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @Schema(description = "위치 정보")
        private ReservationResponse.Location location;

        @Schema(description = "리뷰의 내용")
        private String content;

        @Schema(description = "리뷰 사진 URL")
        private List<String> photoURLList;

        @Schema(description = "작가에 대한 별점")
        private Integer photographerRating;

        @Schema(description = "장소에 대한 별점")
        private Integer placeRating;

        @Schema(description = "좋아요 수")
        private Long likeCount;

        @Schema(description = "댓글 수")
        private Long commentCount;

        @Schema(description = "내가 좋아요를 눌렀는지")
        private Boolean isMeLiked;
    }
}
