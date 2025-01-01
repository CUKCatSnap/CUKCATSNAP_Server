package net.catsnap.domain.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import net.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.photographer.dto.response.PhotographerFullyInformationResponse;
import net.catsnap.domain.reservation.dto.ReservationLocation;
import net.catsnap.domain.search.dto.response.ReviewSearchResponse;

public class SearchResponse {

    @Getter
    @Builder
    public static class PreviewFeedSearchList {

        private List<PreviewFeedSearch> previewFeedSearchList;
    }

    @Getter
    @Builder
    public static class PreviewFeedSearch {

        private PhotographerFullyInformationResponse photographerFullyInformation;

        @Schema(description = "검색된 피드의 id")
        private Long feedId;

        @Schema(description = "위치 정보")
        private ReservationLocation reservationLocation;

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

        private PhotographerFullyInformationResponse photographerFullyInformation;

        @Schema(description = "글 작성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @Schema(description = "위치 정보")
        private ReservationLocation reservationLocation;

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

        private PhotographerFullyInformationResponse photographerFullyInformation;
        private MemberTinyInformationResponse memberTinyInformationResponse;

        @Schema(description = "검색된 리뷰의 id")
        private Long reviewId;

        @Schema(description = "위치 정보")
        private ReservationLocation reservationLocation;

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

        List<ReviewSearchResponse> detailReviewSearchList;
    }
}
