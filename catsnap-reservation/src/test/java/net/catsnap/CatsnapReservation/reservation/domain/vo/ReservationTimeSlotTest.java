package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationTimeSlot VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationTimeSlotTest {

    @Test
    void 유효한_시간대로_생성에_성공한다() {
        // given
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 15, 12, 0);

        // when
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(start, end);

        // then
        assertThat(timeSlot.getStartDateTime()).isEqualTo(start);
        assertThat(timeSlot.getEndDateTime()).isEqualTo(end);
    }

    @Test
    void null_시작시각으로_생성_시_예외가_발생한다() {
        assertThatThrownBy(() -> new ReservationTimeSlot(null, LocalDateTime.of(2025, 6, 15, 12, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시각은 필수입니다");
    }

    @Test
    void null_종료시각으로_생성_시_예외가_발생한다() {
        assertThatThrownBy(() -> new ReservationTimeSlot(LocalDateTime.of(2025, 6, 15, 10, 0), null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("종료 시각은 필수입니다");
    }

    @Test
    void 시작시각이_종료시각_이후면_예외가_발생한다() {
        assertThatThrownBy(() -> new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 14, 0), LocalDateTime.of(2025, 6, 15, 10, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시각은 종료 시각보다 빨라야 합니다");
    }

    @Test
    void 시작시각과_종료시각이_같으면_예외가_발생한다() {
        LocalDateTime sameTime = LocalDateTime.of(2025, 6, 15, 10, 0);

        assertThatThrownBy(() -> new ReservationTimeSlot(sameTime, sameTime))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시각은 종료 시각보다 빨라야 합니다");
    }

    @Test
    void contains는_시간대_내의_시각에_대해_true를_반환한다() {
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));

        assertThat(timeSlot.contains(LocalDateTime.of(2025, 6, 15, 10, 0))).isTrue();
        assertThat(timeSlot.contains(LocalDateTime.of(2025, 6, 15, 11, 0))).isTrue();
    }

    @Test
    void contains는_시간대_밖의_시각에_대해_false를_반환한다() {
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));

        assertThat(timeSlot.contains(LocalDateTime.of(2025, 6, 15, 9, 59))).isFalse();
        assertThat(timeSlot.contains(LocalDateTime.of(2025, 6, 15, 12, 0))).isFalse();
    }

    @Test
    void contains는_null에_대해_false를_반환한다() {
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));

        assertThat(timeSlot.contains(null)).isFalse();
    }

    @Test
    void getReservationDate는_시작시각의_날짜를_반환한다() {
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 22, 0), LocalDateTime.of(2025, 6, 16, 1, 0));

        assertThat(timeSlot.getReservationDate()).isEqualTo(java.time.LocalDate.of(2025, 6, 15));
    }

    @Test
    void duration은_시작과_종료_사이의_시간을_반환한다() {
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 30));

        Duration duration = timeSlot.duration();

        assertThat(duration).isEqualTo(Duration.ofHours(2).plusMinutes(30));
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        ReservationTimeSlot slot1 = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
        ReservationTimeSlot slot2 = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));

        assertThat(slot1).isEqualTo(slot2);
        assertThat(slot1.hashCode()).isEqualTo(slot2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        ReservationTimeSlot slot1 = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
        ReservationTimeSlot slot2 = new ReservationTimeSlot(
            LocalDateTime.of(2025, 6, 16, 10, 0), LocalDateTime.of(2025, 6, 16, 12, 0));

        assertThat(slot1).isNotEqualTo(slot2);
    }

    @Nested
    @DisplayName("overlaps 테스트")
    class OverlapsTest {

        @Test
        void 시간이_겹치면_true를_반환한다() {
            ReservationTimeSlot slot1 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
            ReservationTimeSlot slot2 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 11, 0), LocalDateTime.of(2025, 6, 15, 13, 0));

            assertThat(slot1.overlaps(slot2)).isTrue();
            assertThat(slot2.overlaps(slot1)).isTrue();
        }

        @Test
        void 한쪽이_다른쪽을_완전히_포함하면_true를_반환한다() {
            ReservationTimeSlot outer = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 14, 0));
            ReservationTimeSlot inner = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 11, 0), LocalDateTime.of(2025, 6, 15, 13, 0));

            assertThat(outer.overlaps(inner)).isTrue();
            assertThat(inner.overlaps(outer)).isTrue();
        }

        @Test
        void 끝과_시작이_딱_맞닿으면_false를_반환한다() {
            ReservationTimeSlot slot1 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
            ReservationTimeSlot slot2 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 12, 0), LocalDateTime.of(2025, 6, 15, 14, 0));

            assertThat(slot1.overlaps(slot2)).isFalse();
            assertThat(slot2.overlaps(slot1)).isFalse();
        }

        @Test
        void 완전히_떨어져_있으면_false를_반환한다() {
            ReservationTimeSlot slot1 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
            ReservationTimeSlot slot2 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 14, 0), LocalDateTime.of(2025, 6, 15, 16, 0));

            assertThat(slot1.overlaps(slot2)).isFalse();
        }

        @Test
        void 날짜가_다르면_false를_반환한다() {
            ReservationTimeSlot slot1 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));
            ReservationTimeSlot slot2 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 16, 10, 0), LocalDateTime.of(2025, 6, 16, 12, 0));

            assertThat(slot1.overlaps(slot2)).isFalse();
        }

        @Test
        void 날짜를_넘기는_예약도_겹침을_감지한다() {
            ReservationTimeSlot slot1 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 22, 0), LocalDateTime.of(2025, 6, 16, 1, 0));
            ReservationTimeSlot slot2 = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 23, 0), LocalDateTime.of(2025, 6, 16, 2, 0));

            assertThat(slot1.overlaps(slot2)).isTrue();
        }

        @Test
        void null에_대해_false를_반환한다() {
            ReservationTimeSlot slot = new ReservationTimeSlot(
                LocalDateTime.of(2025, 6, 15, 10, 0), LocalDateTime.of(2025, 6, 15, 12, 0));

            assertThat(slot.overlaps(null)).isFalse();
        }
    }
}