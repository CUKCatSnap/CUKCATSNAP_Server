package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl ReservationService;

    @Spy
    private WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;

    @Test
    @DisplayName("createJoinedPhotographerReservationTimeFormat() : 성공")
    void createJoinedPhotographerReservationTimeFormat() {
        //given
        Photographer photographer = Photographer.builder()
                .identifier("test")
                .build();
        //when
        ReservationService.createJoinedPhotographerReservationTimeFormat(photographer);
        //then
        Mockito.verify(weekdayReservationTimeMappingRepository,Mockito.times(1)).saveAll(Mockito.any());
    }

}