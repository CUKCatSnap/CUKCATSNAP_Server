package net.catsnap.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import net.catsnap.domain.reservation.client.HolidayClient;
import net.catsnap.domain.reservation.document.Holiday;
import net.catsnap.domain.reservation.dto.HolidayListResponse;
import net.catsnap.domain.reservation.repository.HolidayRepository;
import net.catsnap.support.fixture.HolidayFixture;
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
        Holiday holiday1 = HolidayFixture.Holiday()
            .id(LocalDate.of(2024, 1, 1))
            .holidayName("신정")
            .build();
        Holiday holiday2 = HolidayFixture.Holiday()
            .id(LocalDate.of(2024, 12, 25))
            .holidayName("크리스마스")
            .build();
        given(holidayClient.getHolidays()).willReturn(List.of(holiday1, holiday2));
        given(holidayRepository.saveAll(any())).willReturn(null);

        // when
        holidayService.saveHolidays();

        // then
        verify(holidayRepository, times(1)).saveAll(any());
    }

    @Test
    void 공휴일_조회_후_레디스에_저장된_공휴일을_조회한다() {
        // given
        Holiday holiday = HolidayFixture.Holiday()
            .id(LocalDate.of(2024, 1, 1))
            .holidayName("신정")
            .build();
        given(holidayRepository.findById("2021-01-01"))
            .willReturn(Optional.of(holiday));

        given(holidayRepository.findById("2021-01-02"))
            .willReturn(Optional.empty());
        // when
        Boolean IsHoliday = holidayService.isHoliday(LocalDate.of(2021, 1, 1));
        Boolean IsNotHoliday = holidayService.isHoliday(LocalDate.of(2021, 1, 2));

        // then
        Assertions.assertThat(IsHoliday).isTrue();
        Assertions.assertThat(IsNotHoliday).isFalse();
    }

    @Test
    void 특정_달의_공휴일을_조회한다() {
        // given
        Holiday holiday1 = HolidayFixture.Holiday()
            .id(LocalDate.of(2024, 1, 1))
            .holidayName("신정")
            .build();
        Holiday holiday2 = HolidayFixture.Holiday()
            .id(LocalDate.of(2024, 12, 25))
            .holidayName("크리스마스")
            .build();
        given(holidayRepository.findAll()).willReturn(List.of(holiday1, holiday2));

        // when
        HolidayListResponse holidayListResponse = holidayService.getHoliday(YearMonth.of(2024, 1));

        // then
        Assertions.assertThat(holidayListResponse.holidayList().size()).isEqualTo(1);
    }
}
