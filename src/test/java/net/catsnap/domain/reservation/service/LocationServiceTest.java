package net.catsnap.domain.reservation.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.domain.client.ReverseGeocodingClient;
import net.catsnap.domain.client.dto.LegalAddress;
import net.catsnap.domain.reservation.dto.LegalAddressEntity;
import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;
import net.catsnap.domain.reservation.repository.CityLevelRepository;
import net.catsnap.domain.reservation.repository.DistrictLevelRepository;
import net.catsnap.domain.reservation.repository.TownLevelRepository;
import net.catsnap.support.fixture.CityLevelFixture;
import net.catsnap.support.fixture.DistrictLevelFixture;
import net.catsnap.support.fixture.LegalAddressFixture;
import net.catsnap.support.fixture.TownLevelFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private ReverseGeocodingClient reverseGeocodingClient;
    @Mock
    private CityLevelRepository cityLevelRepository;
    @Mock
    private DistrictLevelRepository districtLevelRepository;
    @Mock
    private TownLevelRepository townLevelRepository;


    @Test
    void 위도와_경도로_주소를_알아낸다() {
        // given
        double latitude = 37.575899136489376;
        double longitude = 126.97684688993532;
        LegalAddress legalAddress = LegalAddressFixture.legalAddress()
            .build();
        CityLevel cityLevel = CityLevelFixture.CityLevel()
            .id(1L)
            .build();
        DistrictLevel districtLevel = DistrictLevelFixture.DistrictLevel()
            .id(1L)
            .build();
        TownLevel townLevel = TownLevelFixture.TownLevel()
            .id(1L)
            .build();
        given(reverseGeocodingClient.getLegalAddress(latitude, longitude))
            .willReturn(legalAddress);
        given(cityLevelRepository.findCityLevelByCityName(cityLevel.getCityName()))
            .willReturn(Optional.of(cityLevel));
        given(districtLevelRepository.findDistrictLevelByDistrictName(
            districtLevel.getDistrictName())
        )
            .willReturn(Optional.of(districtLevel));
        given(townLevelRepository.findTownLevelByTownName(townLevel.getTownName()))
            .willReturn(Optional.of(townLevel));

        // when
        LegalAddressEntity legalAddressEntity = locationService.getLegalAddressEntity(latitude,
            longitude);

        //then
        Assertions.assertThat(legalAddressEntity.cityLevel()).isEqualTo(cityLevel);
        Assertions.assertThat(legalAddressEntity.districtLevel())
            .isEqualTo(districtLevel);
        Assertions.assertThat(legalAddressEntity.townLevel()).isEqualTo(townLevel);
    }
}