package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationTimeSlot VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationTimeSlotTest {

    @Test
    void 유효한_시간대로_생성에_성공한다() {
        // given
        LocalDate date = LocalDate.of(2025, 6, 15);
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(12, 0);

        // when
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(date, start, end);

        // then
        assertThat(timeSlot.getReservationDate()).isEqualTo(date);
        assertThat(timeSlot.getStartTime()).isEqualTo(start);
        assertThat(timeSlot.getEndTime()).isEqualTo(end);
    }

    @Test
    void null_날짜로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeSlot(null, LocalTime.of(10, 0), LocalTime.of(12, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("예약 날짜는 필수입니다");
    }

    @Test
    void null_시작시간으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeSlot(LocalDate.of(2025, 6, 15), null, LocalTime.of(12, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시간은 필수입니다");
    }

    @Test
    void null_종료시간으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeSlot(LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("종료 시간은 필수입니다");
    }

    @Test
    void 시작시간이_종료시간_이후면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(14, 0), LocalTime.of(10, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시간은 종료 시간보다 빨라야 합니다");
    }

    @Test
    void 시작시간과_종료시간이_같으면_예외가_발생한다() {
        // given
        LocalTime sameTime = LocalTime.of(10, 0);

        // when & then
        assertThatThrownBy(() -> new ReservationTimeSlot(LocalDate.of(2025, 6, 15), sameTime, sameTime))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시간은 종료 시간보다 빨라야 합니다");
    }

    @Test
    void contains는_시간대_내의_시간에_대해_true를_반환한다() {
        // given
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when & then
        assertThat(timeSlot.contains(LocalTime.of(10, 0))).isTrue();
        assertThat(timeSlot.contains(LocalTime.of(11, 0))).isTrue();
    }

    @Test
    void contains는_시간대_밖의_시간에_대해_false를_반환한다() {
        // given
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when & then
        assertThat(timeSlot.contains(LocalTime.of(9, 59))).isFalse();
        assertThat(timeSlot.contains(LocalTime.of(12, 0))).isFalse(); // 종료 시간은 미포함
    }

    @Test
    void contains는_null에_대해_false를_반환한다() {
        // given
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when & then
        assertThat(timeSlot.contains(null)).isFalse();
    }

    @Test
    void duration은_시작과_종료_사이의_시간을_반환한다() {
        // given
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 30));

        // when
        Duration duration = timeSlot.duration();

        // then
        assertThat(duration).isEqualTo(Duration.ofHours(2).plusMinutes(30));
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        ReservationTimeSlot slot1 = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));
        ReservationTimeSlot slot2 = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when & then
        assertThat(slot1).isEqualTo(slot2);
        assertThat(slot1.hashCode()).isEqualTo(slot2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        ReservationTimeSlot slot1 = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));
        ReservationTimeSlot slot2 = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 16), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when & then
        assertThat(slot1).isNotEqualTo(slot2);
    }

    @Test
    void toString이_날짜와_시간을_반환한다() {
        // given
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDate.of(2025, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0));

        // when
        String result = timeSlot.toString();

        // then
        assertThat(result).isEqualTo("2025-06-15 10:00~12:00");
    }
}
