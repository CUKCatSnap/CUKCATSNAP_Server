package net.catsnap.domain.reservation.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.domain.client.ReverseGeocodingClient;
import net.catsnap.domain.client.dto.LegalAddress;
import net.catsnap.domain.reservation.dto.LegalAddressEntity;
import net.catsnap.domain.reservation.entity.CityLevel;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;
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
            .cityLevel(cityLevel)
            .build();
        TownLevel townLevel = TownLevelFixture.TownLevel()
            .id(1L)
            .districtLevel(districtLevel)
            .build();
        given(reverseGeocodingClient.getLegalAddressCode(latitude, longitude))
            .willReturn(townLevel.getCode());
        given(townLevelRepository.findTownLevelsByCode(townLevel.getCode()))
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