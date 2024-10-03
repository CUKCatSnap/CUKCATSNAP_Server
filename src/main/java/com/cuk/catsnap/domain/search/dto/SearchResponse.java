package com.cuk.catsnap.domain.search.dto;

import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class SearchResponse {

    @Getter
    @Builder
    public static class PreviewFeedSearchList{
        List<PreviewFeedSearch> previewFeedSearchList;
    }

    @Getter
    @Builder
    public static class PreviewFeedSearch {
        public PhotographerResponse.PhotographerFullyInformation photographerTinyInformation;
        @Schema(description = "검색된 피드의 id")
        public Long feedId;
        @Schema(description = "검색에 보여줄 이미지 1장")
        public String firstPhotoUrl;
    }

    @Getter
    @Builder
    public static class PreviewReviewSearchList{
        List<PreviewReviewSearch> previewReviewSearchList;
    }

    @Getter
    @Builder
    public static class PreviewReviewSearch {
        public PhotographerResponse.PhotographerFullyInformation photographerTinyInformation;
        @Schema(description = "검색된 리뷰의 id")
        public Long reviewId;
        @Schema(description = "검색에 보여줄 이미지 1장")
        public String firstPhotoUrl;
    }
}
