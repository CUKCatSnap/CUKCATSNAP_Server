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
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> customTimes = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(customTimes);

        // when
        ScheduleOverride override = ScheduleOverride.create(targetDate, availableTimes);

        // then
        assertThat(override.getTargetDate()).isEqualTo(targetDate);
        assertThat(override.getAvailableTimes()).isEqualTo(availableTimes);
        assertThat(override.getAvailableTimes().isEmpty()).isFalse();
        assertThat(override.getAvailableTimes().size()).isEqualTo(3);
    }

    @Test
    void 휴무로_예외_규칙_생성에_성공한다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // when
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);

        // then
        assertThat(override.getTargetDate()).isEqualTo(targetDate);
        assertThat(override.getAvailableTimes()).isNotNull();
        assertThat(override.getAvailableTimes().isEmpty()).isTrue();
    }

    @Test
    void targetDate가_null이면_예외가_발생한다() {
        // given
        LocalDate targetDate = null;
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.create(targetDate, availableTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대상 날짜는 필수입니다.");
    }

    @Test
    void availableTimes가_null이면_예외가_발생한다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        AvailableStartTimes availableTimes = null;

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.create(targetDate, availableTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("덮어쓴 시간표는 null일 수 없습니다.");
    }

    @Test
    void dayOff_팩토리_메서드로_생성_시_targetDate가_null이면_예외가_발생한다() {
        // given
        LocalDate targetDate = null;

        // when & then
        assertThatThrownBy(() -> ScheduleOverride.dayOff(targetDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("대상 날짜는 필수입니다.");
    }

    @Test
    void 커스텀_시간_설정_시_예약_가능_시간이_올바르게_설정된다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> customTimes = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(customTimes);

        // when
        ScheduleOverride override = ScheduleOverride.create(targetDate, availableTimes);

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
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);

        // when
        String result = override.toString();

        // then
        assertThat(result).contains("ScheduleOverride");
        assertThat(result).contains("targetDate=" + targetDate);
    }

    @Test
    void 예약_가능_시간을_변경할_수_있다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);
        List<LocalTime> newTimes = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(15, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(newTimes);

        // when
        override.updateAvailableTimes(availableTimes);

        // then
        assertThat(override.getAvailableTimes()).isEqualTo(availableTimes);
        assertThat(override.hasAvailableTimes()).isTrue();
    }

    @Test
    void 예약_가능_시간_변경_시_null이면_예외가_발생한다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);

        // when & then
        assertThatThrownBy(() -> override.updateAvailableTimes(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 시간은 null일 수 없습니다.");
    }

    @Test
    void 휴무일로_변경할_수_있다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);
        ScheduleOverride override = ScheduleOverride.create(targetDate, availableTimes);

        // when
        override.changeToDayOff();

        // then
        assertThat(override.hasAvailableTimes()).isFalse();
        assertThat(override.getAvailableTimes().isEmpty()).isTrue();
    }

    @Test
    void 예약_가능_시간이_있는지_확인할_수_있다() {
        // given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        List<LocalTime> times = List.of(LocalTime.of(9, 0));
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);
        ScheduleOverride workingOverride = ScheduleOverride.create(targetDate, availableTimes);
        ScheduleOverride dayOffOverride = ScheduleOverride.dayOff(targetDate);

        // when & then
        assertThat(workingOverride.hasAvailableTimes()).isTrue();
        assertThat(dayOffOverride.hasAvailableTimes()).isFalse();
    }
}
