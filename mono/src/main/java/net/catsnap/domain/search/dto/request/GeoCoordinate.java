package net.catsnap.domain.search.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record GeoCoordinate(
    @Schema(description = "위도값(y 좌표값)")
    Double latitude,
    @Schema(description = "경도값(x 좌표값)")
    Double longitude
) {

}
