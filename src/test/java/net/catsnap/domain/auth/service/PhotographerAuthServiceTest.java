package net.catsnap.domain.auth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import net.catsnap.domain.auth.dto.photographer.request.PhotographerSignUpRequest;
import net.catsnap.domain.reservation.service.PhotographerReservationService;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerSettingRepository;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import net.catsnap.support.fixture.PhotographerSignUpRequestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerAuthServiceTest {

    @InjectMocks
    private PhotographerAuthService photographerAuthService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PhotographerRepository photographerRepository;
    @Mock
    private PhotographerReservationService photographerReservationService;
    @Mock
    private PhotographerSettingRepository photographerSettingRepository;
    @Mock
    private PhotographerReservationNoticeRepository photographerReservationNoticeRepository;
    @Mock
    private PhotographerReservationLocationRepository photographerReservationLocationRepository;
    @Mock
    private UserRepository userRepository;

    @Nested
    class 작가_회원_가입_테스트 {

        @Test
        void 회원_가입_성공() {
            //given
            PhotographerSignUpRequest photographerSignUpRequest = PhotographerSignUpRequestFixture.photographerSignUpRequest()
                .password("rawPassword")
                .build();
            given(userRepository.findByIdentifier(
                photographerSignUpRequest.identifier())).willReturn(
                Optional.empty());
            given(passwordEncoder.encode(photographerSignUpRequest.password())).willReturn(
                "hashedPassword");
            doNothing().when(photographerReservationService)
                .createJoinedPhotographerReservationTimeFormat(
                    any());

            //when
            photographerAuthService.singUp(photographerSignUpRequest);

            //then
            verify(photographerRepository, times(1))
                .save(argThat(photographer -> photographer.getPassword().equals("hashedPassword")));
        }

        @Test
        void 중복된_아이디로_회원_가입_실패() {
            //given
            PhotographerSignUpRequest photographerSignUpRequest = PhotographerSignUpRequestFixture.photographerSignUpRequest()
                .password("rawPassword")
                .build();
            Photographer photographer = Photographer.builder()
                .identifier(photographerSignUpRequest.identifier())
                .build();

            given(userRepository.findByIdentifier(
                photographerSignUpRequest.identifier())).willReturn(
                Optional.of(photographer));

            //when, then
            Assertions.assertThatThrownBy(
                    () -> photographerAuthService.singUp(photographerSignUpRequest))
                .isInstanceOf(DuplicatedPhotographerException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
            verify(photographerRepository, times(0))
                .save(any());
        }

    }
}