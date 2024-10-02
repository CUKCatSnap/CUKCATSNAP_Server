package com.cuk.catsnap.domain.social.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class SocialResponse {

    @Getter
    @Builder
    public static class subscribePlaceList{
        List<subscribePlace> subscribePlaceList;
    }

    @Getter
    @Builder
    public static class subscribePlace{
        String keyword;
    }
}
