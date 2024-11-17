package com.cuk.catsnap.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cuk.catsnap.domain.reservation.client.HolidayClient;
import com.cuk.catsnap.domain.reservation.document.Holiday;
import com.cuk.catsnap.domain.reservation.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.List;
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
class HolidayServiceTest {

    @InjectMocks
    private HolidayService holidayService;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private HolidayClient holidayClient;

    @Test
    void 공휴일을_조회한_후_레디스에_저장한다() {
        // given
        given(holidayClient.getHolidays()).willReturn(List.of(
            LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 1)));
        given(holidayRepository.save(any())).willReturn(null);

        // when
        holidayService.saveHolidays();

        // then
        verify(holidayRepository, times(2)).save(any(Holiday.class));
    }

}