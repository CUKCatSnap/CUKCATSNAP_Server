package net.catsnap.CatsnapReservation.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("ScheduleOverride 도메인 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ScheduleOverrideTest {

    @Test
    void 커스텀_시간으로_예외_규칙_생성에_성공한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> customTimes = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(customTimes);

        // when
        ScheduleOverride override = ScheduleOverride.create(
            photographerId,
            targetDate,
            availableTimes
        );

        // then
        assertThat(override.getPhotographerId()).isEqualTo(photographerId);
        assertThat(override.getTargetDate()).isEqualTo(targetDate);
        assertThat(override.getAvailableTimes()).isEqualTo(availableTimes);
        assertThat(override.getAvailableTimes().isEmpty()).isFalse();
        assertThat(override.getAvailableTimes().size()).isEqualTo(3);
    }

    @Test
    void 휴무로_예외_규칙_생성에_성공한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // when
        ScheduleOverride override = ScheduleOverride.dayOff(photographerId, targetDate);

        // then
        assertThat(override.getPhotographerId()).isEqualTo(photographerId);
        assertThat(override.getTargetDate()).isEqualTo(targetDate);
        assertThat(override.getAvailableTimes()).isNotNull();
        assertThat(override.getAvailableTimes().isEmpty()).isTrue();
    }

    @Test
    void photographerId가_null이면_예외가_발생한다() {
        // given
        Long photographerId = null;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.create(photographerId, targetDate, availableTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작가 ID는 필수입니다.");
    }

    @Test
    void targetDate가_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = null;
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.create(photographerId, targetDate, availableTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대상 날짜는 필수입니다.");
    }

    @Test
    void availableTimes가_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        AvailableStartTimes availableTimes = null;

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.create(photographerId, targetDate, availableTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("덮어쓴 시간표는 null일 수 없습니다.");
    }

    @Test
    void dayOff_팩토리_메서드로_생성_시_photographerId가_null이면_예외가_발생한다() {
        // given
        Long photographerId = null;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.dayOff(photographerId, targetDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작가 ID는 필수입니다.");
    }

    @Test
    void dayOff_팩토리_메서드로_생성_시_targetDate가_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = null;

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.dayOff(photographerId, targetDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대상 날짜는 필수입니다.");
    }

    @Test
    void 커스텀_시간_설정_시_예약_가능_시간이_올바르게_설정된다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> customTimes = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(customTimes);

        // when
        ScheduleOverride override = ScheduleOverride.create(
            photographerId,
            targetDate,
            availableTimes
        );

        // then
        assertThat(override.getAvailableTimes().toList()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
    }

    @Test
    void toString이_올바르게_동작한다() {
        // given
        Long photographerId = 1L;
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        ScheduleOverride override = ScheduleOverride.dayOff(photographerId, targetDate);

        // when
        String result = override.toString();

        // then
        assertThat(result).contains("AvailabilityOverride");
        assertThat(result).contains("photographerId=" + photographerId);
        assertThat(result).contains("targetDate=" + targetDate);
    }
}
