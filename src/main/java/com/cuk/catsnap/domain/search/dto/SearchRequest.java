package com.cuk.catsnap.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchRequest {

    @Getter
    @NoArgsConstructor
    public static class FeedSearchParameter{
        @Schema(description = "검색어")
        private String searchTerm;
        @Schema(description = "검색할 화면의 좌표값")
        private GeoSearchParameter geoSearchParameter;
        @Schema(description = "작가의 id")
        private Long photographerId;
        @Schema(description = "내가 구독한 작가의 피드만을 보려면 true, 아니면 false")
        private Boolean isMySubscribePhotographer;
    }

    @Getter
    @NoArgsConstructor
    public static class GeoSearchParameter{
        @Schema(description = "검색할 화면의 좌상단의 좌표")
        private LocationParameter upperLeft;
        @Schema(description = "검색할 화면의 우하단의 좌표")
        private LocationParameter lowerRight;
    }

    @Getter
    @NoArgsConstructor
    public static class LocationParameter{
        @Schema(description = "위도값")
        private double lat;
        @Schema(description = "경도값")
        private double lng;
    }
}
