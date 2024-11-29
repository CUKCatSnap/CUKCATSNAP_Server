package net.catsnap.domain.photographer.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.domain.photographer.dto.response.PhotographerTinyInformationResponse;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.support.fixture.PhotographerFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerServiceTest {

    @InjectMocks
    private PhotographerService photographerService;

    @Mock
    private PhotographerRepository photographerRepository;

    @Nested
    class 작가의_tinyInformation_조회 {

        @Test
        void 작가의_tiny_information을_조회한다() {
            // given
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            given(photographerRepository.findById(photographer.getId()))
                .willReturn(Optional.of(photographer));

            // when
            PhotographerTinyInformationResponse photographerTinyInformation
                = photographerService.getPhotographerTinyInformation(photographer.getId());

            // then
            Assertions.assertThat(photographerTinyInformation.photographerId())
                .isEqualTo(photographer.getId());
            Assertions.assertThat(photographerTinyInformation.nickname())
                .isEqualTo(photographer.getNickname());
            Assertions.assertThat(photographerTinyInformation.profilePhotoUrl())
                .isEqualTo(photographer.getProfilePhotoUrl());
        }

        @Test
        void 존재하지_않는_작가의_tiny_information을_조회하면_예외가_발생한다() {
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            given(photographerRepository.findById(photographer.getId()))
                .willReturn(Optional.empty());

            Assertions.assertThatThrownBy(
                    () -> photographerService.getPhotographerTinyInformation(photographer.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
        }
    }

}