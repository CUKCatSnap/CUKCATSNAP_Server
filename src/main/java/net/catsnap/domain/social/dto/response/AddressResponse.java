package net.catsnap.domain.social.dto.response;

import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;

public record AddressResponse(
    Long id,
    String code,
    String addressName
) {

    public static AddressResponse from(
        CityLevel cityLevel
    ) {
        return new AddressResponse(
            cityLevel.getId(),
            cityLevel.getCode(),
            cityLevel.getCityName()
        );
    }

    public static AddressResponse from(
        DistrictLevel districtLevel
    ) {
        return new AddressResponse(
            districtLevel.getId(),
            districtLevel.getCode(),
            districtLevel.getDistrictName()
        );
    }

    public static AddressResponse from(
        TownLevel townLevel
    ) {
        return new AddressResponse(
            townLevel.getId(),
            townLevel.getCode(),
            townLevel.getTownName()
        );
    }
}
