package net.catsnap.domain.reservation.service;

import static org.mockito.BDDMockito.given;

import net.catsnap.domain.reservation.entity.Weekday;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WeekdayServiceTest {

    @InjectMocks
    private WeekdayService weekdayService;

    @Mock
    private HolidayService holidayService;

    @ParameterizedTest
    @CsvSource({
        "2025-01-01, HOLIDAY, true",  // 공휴일
        "2025-01-02, THURSDAY, false", // 평일(목요일)
        "2025-01-03, FRIDAY, false"  // 평일(금요일)
    })
    void 요일과_공휴일_조회(LocalDate date, Weekday expectedWeekday, boolean isHoliday) {
        // given
        given(holidayService.isHoliday(date))
            .willReturn(isHoliday);

        // when
        Weekday weekday = weekdayService.getWeekday(date);

        // then
        Assertions.assertThat(weekday).isEqualTo(expectedWeekday);
    }

}