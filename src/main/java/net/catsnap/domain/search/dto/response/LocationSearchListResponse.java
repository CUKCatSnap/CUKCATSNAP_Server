package net.catsnap.domain.search.dto.response;

import java.util.List;

public record LocationSearchListResponse(
    List<LocationSearchResponse> locationSearchResponseList
) {

}
