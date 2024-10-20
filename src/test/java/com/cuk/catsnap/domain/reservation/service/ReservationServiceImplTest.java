package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.converter.ReservationConverter;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.mockStatic;


@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl ReservationService;

    @Spy
    private WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;

    @Mock
    private ReservationTimeFormatRepository reservationTimeFormatRepository;

    @Spy
    private ReservationConverter reservationConverter;


    @Test
    @DisplayName("createJoinedPhotographerReservationTimeFormat() : 성공")
    public void createJoinedPhotographerReservationTimeFormat() {
        //given
        Photographer photographer = Photographer.builder()
                .identifier("test")
                .build();
        //when
        ReservationService.createJoinedPhotographerReservationTimeFormat(photographer);
        //then
        Mockito.verify(weekdayReservationTimeMappingRepository,Mockito.times(1)).saveAll(Mockito.any());
    }

    @Test
    @DisplayName("createReservationTimeFormat() : 저장 로직 성공")
    public void createReservationTimeFormat() {
        //given
        MockedStatic<GetAuthenticationInfo> mockedStatic = mockStatic(GetAuthenticationInfo.class);
        mockedStatic.when(GetAuthenticationInfo::getUserId).thenReturn(1L);
        ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat = ReservationRequest.PhotographerReservationTimeFormat.builder()
                .formatName("test")
                .startTimeList(List.of(LocalTime.of(10,0), LocalTime.of(11,0)))
                .build();
        Mockito.when(reservationTimeFormatRepository.save(Mockito.any())).thenReturn(ReservationTimeFormat.builder().id("testId").build());

        //when
        ReservationService.createReservationTimeFormat(photographerReservationTimeFormat,null);
        //then
        Mockito.verify(reservationTimeFormatRepository,Mockito.times(1)).save(Mockito.any());
    }
}