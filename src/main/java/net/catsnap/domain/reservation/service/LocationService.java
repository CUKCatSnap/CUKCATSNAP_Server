package net.catsnap.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.client.ReverseGeocodingClient;
import net.catsnap.domain.client.dto.LegalAddress;
import net.catsnap.domain.reservation.dto.LegalAddressEntity;
import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;
import net.catsnap.domain.reservation.repository.CityLevelRepository;
import net.catsnap.domain.reservation.repository.DistrictLevelRepository;
import net.catsnap.domain.reservation.repository.TownLevelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ReverseGeocodingClient reverseGeocodingClient;
    private final CityLevelRepository cityLevelRepository;
    private final DistrictLevelRepository districtLevelRepository;
    private final TownLevelRepository townLevelRepository;

    public LegalAddressEntity getLegalAddressEntity(Double latitude, Double longitude) {
        LegalAddress legalAddress = reverseGeocodingClient.getLegalAddress(latitude, longitude);
        CityLevel cityLevel = cityLevelRepository.findCityLevelByCityName(
                legalAddress.getCityName())
            .orElse(null);
        DistrictLevel districtLevel = districtLevelRepository.findDistrictLevelByDistrictName(
                legalAddress.getDistrictName())
            .orElse(null);
        TownLevel townLevel = townLevelRepository.findTownLevelByTownName(
                legalAddress.getTownName())
            .orElse(null);

        return LegalAddressEntity.of(cityLevel, districtLevel, townLevel);
    }

}
