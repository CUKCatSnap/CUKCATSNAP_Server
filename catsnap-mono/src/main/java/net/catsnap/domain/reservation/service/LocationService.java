package net.catsnap.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.client.ReverseGeocodingClient;
import net.catsnap.domain.reservation.dto.LegalAddressEntity;
import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;
import net.catsnap.domain.reservation.repository.TownLevelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ReverseGeocodingClient reverseGeocodingClient;
    private final TownLevelRepository townLevelRepository;

    public LegalAddressEntity getLegalAddressEntity(Double latitude, Double longitude) {
        String legalAddressCode = reverseGeocodingClient.getLegalAddressCode(latitude, longitude);
        //todo null 처리
        TownLevel townLevel = townLevelRepository.findTownLevelsByCode(legalAddressCode)
            .orElse(null);
        DistrictLevel districtLevel = townLevel.getDistrictLevel();
        CityLevel cityLevel = districtLevel.getCityLevel();

        return LegalAddressEntity.of(cityLevel, districtLevel, townLevel);
    }
}
