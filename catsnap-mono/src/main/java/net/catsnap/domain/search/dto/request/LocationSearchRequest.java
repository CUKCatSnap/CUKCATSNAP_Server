package net.catsnap.domain.search.dto.request;

public record LocationSearchRequest(
    GeoCoordinate TopLeftCoordinate,
    GeoCoordinate BottomRightCoordinate
) {

}
