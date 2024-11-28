package net.catsnap.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import net.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import net.catsnap.support.fixture.PhotographerFixture;
import net.catsnap.support.fixture.PhotographerSettingFixture;
import net.catsnap.support.fixture.ReservationFixture;
import net.catsnap.support.fixture.ReservationTimeFormatFixture;
import net.catsnap.support.fixture.WeekdayReservationTimeMappingFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationValidatorServiceTest {

    @InjectMocks
    private ReservationValidatorService reservationValidatorService;

    @Mock
    private WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    @Mock
    private ReservationTimeFormatRepository reservationTimeFormatRepository;
    @Mock
    private WeekdayService weekdayService;
    @Mock
    private ReservationRepository reservationRepository;

    @ParameterizedTest
    @CsvSource({
        "1, 7, true",
        "14, 7, false"
    })
    void 작가가_설정한_예약일을_이내이면_true_아니면_false(Long reservationDays, Long preReservationDays,
        Boolean expected) {
        //given
        LocalDateTime reservationDateTime = LocalDateTime.now().plusDays(reservationDays);
        PhotographerSetting photographerSetting = PhotographerSettingFixture.photographerSetting()
            .preReservationDays(preReservationDays)
            .build();

        //when
        boolean result = reservationValidatorService.isWithinAllowedDays(reservationDateTime,
            photographerSetting);
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "3, true",
        "-2, false"
    })
    void 현재시간보다_이후이면_ture_아니면_false(Long reservationDays, Boolean expected) {
        //given
        LocalDateTime reservationDateTime = LocalDateTime.now().plusDays(reservationDays);
        //when
        boolean result = reservationValidatorService.isAfterNow(reservationDateTime);
        //then
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Nested
    class 예약시간이_예약시간포맷에_있는지_테스트 {

        @Test
        void 예약시간이_예약시간포맷에_존재하면_ture() {
            //given
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            WeekdayReservationTimeMapping weekdayReservationTimeMapping = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
                .photographer(photographer)
                .weekday(Weekday.MONDAY)
                .build();
            ReservationTimeFormat reservationTimeFormat = ReservationTimeFormatFixture.reservationTimeFormat()
                .build();
            given(weekdayService.getWeekday(any())).willReturn(Weekday.MONDAY);
            given(weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographer.getId(), Weekday.MONDAY))
                .willReturn(Optional.of(weekdayReservationTimeMapping));
            given(reservationTimeFormatRepository.findById(
                weekdayReservationTimeMapping.getReservationTimeFormatId()))
                .willReturn(reservationTimeFormat);
            LocalDateTime reservationDateTime = LocalDateTime.now()
                .withHour(reservationTimeFormat.getStartTimeList().get(0).getHour())
                .withMinute(reservationTimeFormat.getStartTimeList().get(0).getMinute())
                .withSecond(0)
                .withNano(0);

            //when
            Boolean result = reservationValidatorService.isValidStartTimeInTimeFormat(
                reservationDateTime,
                photographer.getId());

            Assertions.assertThat(result).isTrue();
        }

        @Test
        void 예약시간이_예약시간포맷에_존재하지_않으면_false() {
            //given
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            WeekdayReservationTimeMapping weekdayReservationTimeMapping = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
                .photographer(photographer)
                .weekday(Weekday.MONDAY)
                .build();
            ReservationTimeFormat reservationTimeFormat = ReservationTimeFormatFixture.reservationTimeFormat()
                .startTimeList(List.of(LocalTime.of(10, 0), LocalTime.of(11, 0)))
                .build();
            given(weekdayService.getWeekday(any())).willReturn(Weekday.MONDAY);
            given(weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographer.getId(), Weekday.MONDAY))
                .willReturn(Optional.of(weekdayReservationTimeMapping));
            given(reservationTimeFormatRepository.findById(
                weekdayReservationTimeMapping.getReservationTimeFormatId()))
                .willReturn(reservationTimeFormat);
            LocalDateTime reservationDateTime = LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(0)
                .withNano(0);

            //when
            Boolean result = reservationValidatorService.isValidStartTimeInTimeFormat(
                reservationDateTime,
                photographer.getId());

            //then
            Assertions.assertThat(result).isFalse();
        }

        @Test
        void 작가가_해당_요일에_예약을_막은_경우_false() {
            //given
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            WeekdayReservationTimeMapping weekdayReservationTimeMapping = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
                .photographer(photographer)
                .weekday(Weekday.MONDAY)
                .reservationTimeFormatId(null)
                .build();
            given(weekdayService.getWeekday(any())).willReturn(Weekday.MONDAY);
            given(weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographer.getId(), Weekday.MONDAY))
                .willReturn(Optional.of(weekdayReservationTimeMapping));
            LocalDateTime reservationDateTime = LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(0)
                .withNano(0);

            //when
            Boolean result = reservationValidatorService.isValidStartTimeInTimeFormat(
                reservationDateTime,
                photographer.getId());

            //then
            Assertions.assertThat(result).isFalse();
        }
    }

    @Nested
    class 중복된_예약을_확인 {

        @Test
        void 중복된_예약이_없어서_true() {
            //given
            LocalDateTime reservationDateTime = LocalDateTime.now();
            Long programDurationMinutes = 60L;
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            Reservation reservedReservation1 = ReservationFixture.reservation()
                .startTime(LocalDateTime.now().minusHours(3))
                .endTime(LocalDateTime.now().minusHours(2))
                .build();

            Reservation reservedReservation2 = ReservationFixture.reservation()
                .startTime(LocalDateTime.now().plusHours(2))
                .endTime(LocalDateTime.now().plusHours(3))
                .build();
            given(
                reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
                    any(), any(), any()))
                .willReturn(List.of(reservedReservation1, reservedReservation2));

            //when
            Boolean result = reservationValidatorService.isNotOverBooking(
                reservationDateTime, photographer.getId(), programDurationMinutes);

            //then
            Assertions.assertThat(result).isTrue();
        }

        @Test
        void 겹치는_시간의_예약이_있어_false() {
            //given
            LocalDateTime reservationDateTime = LocalDateTime.now();
            Long programDurationMinutes = 60L;
            Photographer photographer = PhotographerFixture.photographer()
                .id(1L)
                .build();
            Reservation reservedReservation1 = ReservationFixture.reservation()
                .startTime(LocalDateTime.now().minusHours(1))
                .endTime(LocalDateTime.now().plusHours(1))
                .build();

            Reservation reservedReservation2 = ReservationFixture.reservation()
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().plusHours(3))
                .build();
            given(
                reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
                    any(), any(), any()))
                .willReturn(List.of(reservedReservation1, reservedReservation2));

            //when
            Boolean result = reservationValidatorService.isNotOverBooking(
                reservationDateTime, photographer.getId(), programDurationMinutes);

            //then
            Assertions.assertThat(result).isFalse();
        }
    }
}