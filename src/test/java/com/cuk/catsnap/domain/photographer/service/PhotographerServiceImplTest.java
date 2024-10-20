package com.cuk.catsnap.domain.photographer.service;

import com.cuk.catsnap.domain.photographer.converter.PhotographerConverter;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.domain.reservation.service.ReservationService;
import com.cuk.catsnap.domain.reservation.service.ReservationServiceImpl;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberIdException;
import com.cuk.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Tag("member_login")
@ExtendWith(MockitoExtension.class)
class PhotographerServiceImplTest {

    @InjectMocks
    private PhotographerServiceImpl photographerService;

    @Mock
    private PhotographerRepository photographerRepository;

    @Mock
    private PhotographerConverter photographerConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private ReservationService reservationService;

    @Test
    @DisplayName("singUp() : 회원가입 테스트 - 성공 ")
    public void singUp() {

        //given
        PhotographerRequest.PhotographerSignUp photographerSignUp =
                PhotographerRequest.PhotographerSignUp.builder()
                        .identifier("test")
                        .password("password")
                        .build();

        Photographer photographer
                = Photographer.builder()
                .identifier("test")
                .password("encoded_password")
                .build();

        Mockito.when(passwordEncoder.encode("password"))
                .thenReturn("encoded_password");

        Mockito.when(photographerRepository.findByIdentifier("test"))
                .thenReturn(Optional.empty());

        Mockito.when(photographerConverter.photographerSignUpToPhotographer(photographerSignUp, "encoded_password"))
                .thenReturn(Photographer.builder()
                        .identifier("test")
                        .password("encoded_password")
                        .build());

        //when
        photographerService.singUp(photographerSignUp);

        //then
        Mockito.verify(photographerRepository).findByIdentifier(photographerSignUp.getIdentifier());
        Mockito.verify(passwordEncoder).encode(photographerSignUp.getPassword());
        Mockito.verify(photographerConverter).photographerSignUpToPhotographer(photographerSignUp, passwordEncoder.encode("password"));
        Mockito.verify(photographerRepository).save(Mockito.any(Photographer.class));
        Mockito.verify(reservationService).createJoinedPhotographerReservationTimeFormat(Mockito.any(Photographer.class));
    }

    @Test
    @DisplayName("singUp() : 회원가입 테스트 - 실패 : 중복된 id입력 ")
    public void duplicatedId() {

        //given
        PhotographerRequest.PhotographerSignUp photographerSignUp =
                PhotographerRequest.PhotographerSignUp.builder()
                        .identifier("test")
                        .password("password")
                        .build();

        Mockito.when(photographerRepository.findByIdentifier("test"))
                .thenReturn(Optional.of(Photographer.builder()
                        .identifier("test")
                        .password("encoded_password")
                        .build()));

        //when, then
        Assertions.assertThatThrownBy(()->photographerService.singUp(photographerSignUp))
                .isInstanceOf(DuplicatedPhotographerException.class);

        Mockito.verify(photographerRepository, Mockito.never()).save(Mockito.any(Photographer.class));
        Mockito.verify(reservationService, Mockito.never()).createJoinedPhotographerReservationTimeFormat(Mockito.any(Photographer.class));
    }
}