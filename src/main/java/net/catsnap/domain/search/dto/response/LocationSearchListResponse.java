package net.catsnap.domain.search.dto.response;

import java.util.List;

public record LocationSearchListResponse(
    List<LocationSearchResponse> locationSearchResponseList
) {

    public static LocationSearchListResponse from(
        List<LocationSearchResponse> locationSearchResponseList
    ) {
        return new LocationSearchListResponse(
            locationSearchResponseList
        );
    }
}
