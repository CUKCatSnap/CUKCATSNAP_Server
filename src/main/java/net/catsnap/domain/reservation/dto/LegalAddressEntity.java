package net.catsnap.domain.reservation.dto;

import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;

public record LegalAddressEntity(
    CityLevel cityLevel,
    DistrictLevel districtLevel,
    TownLevel townLevel
) {

    public static LegalAddressEntity of(CityLevel cityLevel, DistrictLevel districtLevel,
        TownLevel townLevel) {
        return new LegalAddressEntity(cityLevel, districtLevel, townLevel);
    }
}
