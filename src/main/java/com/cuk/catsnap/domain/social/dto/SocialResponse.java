package com.cuk.catsnap.domain.social.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class SocialResponse {

    @Getter
    @Builder
    public static class subscribePlaceList {

        private List<subscribePlace> subscribePlaceList;
    }

    @Getter
    @Builder
    public static class subscribePlace {

        private Long placeSubscribeId;
        private String keyword;
        private LocalDateTime createdAt;
    }
}
