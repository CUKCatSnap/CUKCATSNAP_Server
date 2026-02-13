package net.catsnap.domain.search.dto.response;

import java.util.List;

public record ReviewSearchListResponse(
    List<ReviewSearchResponse> reviewSearchResponseList
) {

    public static ReviewSearchListResponse of(
        List<ReviewSearchResponse> reviewSearchResponseList
    ) {
        return new ReviewSearchListResponse(
            reviewSearchResponseList
        );
    }
}
