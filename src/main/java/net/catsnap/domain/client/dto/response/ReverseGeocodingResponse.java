package net.catsnap.domain.client.dto.response;

import java.util.List;
import net.catsnap.domain.client.dto.LegalAddress;

public record ReverseGeocodingResponse(
    Status status,
    List<Result> results
) {

    public record Status(
        Integer code,
        String name,
        String message
    ) {

    }

    public record Result(
        String name,
        Code code,
        Region region
    ) {

    }

    public record Code(
        String id,
        String type,
        String mappingId
    ) {

    }

    public record Region(
        Area area0,
        Area area1,
        Area area2,
        Area area3,
        Area area4
    ) {

    }

    public record Area(
        String name,
        Coords coords,
        String alias
    ) {

    }

    public record Coords(
        Center center
    ) {

    }

    public record Center(
        String crs,
        Double x,
        Double y
    ) {

    }

    public LegalAddress toLegalAddress() {
        return new LegalAddress(
            results.get(0).region.area1.name,
            results.get(0).region.area1.name,
            results.get(0).region.area3.name
        );
    }
}