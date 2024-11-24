package net.catsnap.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.catsnap.domain.reservation.client.HolidayClient;
import net.catsnap.domain.reservation.document.Holiday;
import net.catsnap.domain.reservation.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        given(holidayClient.getHolidays()).willReturn(List.of(
            LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 1)));
        given(holidayRepository.save(any())).willReturn(null);

        // when
        holidayService.saveHolidays();

        // then
        verify(holidayRepository, times(2)).save(any(Holiday.class));
    }

    @Test
    void 공휴일_조회_후_레디스에_저장된_공휴일을_조회한다() {
        // given
        given(holidayRepository.findById("2021-01-01"))
            .willReturn(Optional.of(new Holiday(LocalDate.of(2021, 1, 1))));

        given(holidayRepository.findById("2021-01-02"))
            .willReturn(Optional.empty());
        // when
        Boolean holiday = holidayService.isHoliday(LocalDate.of(2021, 1, 1));
        Boolean notHoliday = holidayService.isHoliday(LocalDate.of(2021, 1, 2));

        // then
        Assertions.assertThat(holiday).isTrue();
        Assertions.assertThat(notHoliday).isFalse();
    }
}
