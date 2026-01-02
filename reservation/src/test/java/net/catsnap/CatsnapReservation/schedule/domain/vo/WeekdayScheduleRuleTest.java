package net.catsnap.CatsnapReservation.schedule.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("WeekdayScheduleRule VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WeekdayScheduleRuleTest {

    @Test
    void 근무일_규칙_생성에_성공한다() {
        // given
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        );
        AvailableStartTimes availableStartTimes = AvailableStartTimes.of(times);

        // when
        WeekdayScheduleRule rule = WeekdayScheduleRule.workingDay(dayOfWeek, availableStartTimes);

        // then
        assertThat(rule.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(rule.isWorkingDay()).isTrue();
        assertThat(rule.getAvailableStartTimes()).isEqualTo(availableStartTimes);
    }

    @Test
    void List로_근무일_규칙_생성에_성공한다() {
        // given
        DayOfWeek dayOfWeek = DayOfWeek.TUESDAY;
        List<LocalTime> times = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(15, 0)
        );

        // when
        WeekdayScheduleRule rule = WeekdayScheduleRule.workingDay(dayOfWeek, times);

        // then
        assertThat(rule.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(rule.isWorkingDay()).isTrue();
        assertThat(rule.generateAvailableTimes()).containsExactly(
            LocalTime.of(14, 0),
            LocalTime.of(15, 0)
        );
    }

    @Test
    void 휴무일_규칙_생성에_성공한다() {
        // given
        DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;

        // when
        WeekdayScheduleRule rule = WeekdayScheduleRule.dayOff(dayOfWeek);

        // then
        assertThat(rule.getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(rule.isWorkingDay()).isFalse();
        assertThat(rule.getAvailableStartTimes().isEmpty()).isTrue();
    }

    @Test
    void dayOfWeek가_null이면_예외가_발생한다() {
        // given
        DayOfWeek dayOfWeek = null;
        List<LocalTime> times = List.of(LocalTime.of(9, 0));

        // when & then
        assertThatThrownBy(() -> WeekdayScheduleRule.workingDay(dayOfWeek, times))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요일은 필수입니다.");
    }

    @Test
    void availableStartTimes가_null이면_예외가_발생한다() {
        // given
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        AvailableStartTimes availableStartTimes = null;

        // when & then
        assertThatThrownBy(() -> WeekdayScheduleRule.workingDay(dayOfWeek, availableStartTimes))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 시간은 필수입니다.");
    }

    @Test
    void 예약_가능_시간을_생성할_수_있다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        WeekdayScheduleRule rule = WeekdayScheduleRule.workingDay(DayOfWeek.WEDNESDAY, times);

        // when
        List<LocalTime> availableTimes = rule.generateAvailableTimes();

        // then
        assertThat(availableTimes).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
    }

    @Test
    void 휴무일은_예약_가능_시간이_없다() {
        // given
        WeekdayScheduleRule rule = WeekdayScheduleRule.dayOff(DayOfWeek.SATURDAY);

        // when
        List<LocalTime> availableTimes = rule.generateAvailableTimes();

        // then
        assertThat(availableTimes).isEmpty();
    }

    @Test
    void 특정_시간이_유효한_예약_시작_시간인지_확인할_수_있다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        WeekdayScheduleRule rule = WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, times);

        // when & then
        assertThat(rule.isValidStartTime(LocalTime.of(9, 0))).isTrue();
        assertThat(rule.isValidStartTime(LocalTime.of(10, 0))).isTrue();
        assertThat(rule.isValidStartTime(LocalTime.of(11, 0))).isFalse();
    }

    @Test
    void 휴무일은_모든_시간이_유효하지_않다() {
        // given
        WeekdayScheduleRule rule = WeekdayScheduleRule.dayOff(DayOfWeek.SUNDAY);

        // when & then
        assertThat(rule.isValidStartTime(LocalTime.of(9, 0))).isFalse();
        assertThat(rule.isValidStartTime(LocalTime.of(14, 0))).isFalse();
    }

    @Test
    void 동일한_내용의_규칙은_같다() {
        // given
        List<LocalTime> times = List.of(LocalTime.of(9, 0), LocalTime.of(10, 0));
        WeekdayScheduleRule rule1 = WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, times);
        WeekdayScheduleRule rule2 = WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, times);

        // when & then
        assertThat(rule1).isEqualTo(rule2);
        assertThat(rule1.hashCode()).isEqualTo(rule2.hashCode());
    }

    @Test
    void toString이_근무일에_대해_올바르게_동작한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        WeekdayScheduleRule rule = WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, times);

        // when
        String result = rule.toString();

        // then
        assertThat(result).contains("MONDAY");
        assertThat(result).doesNotContain("휴무");
    }

    @Test
    void toString이_휴무일에_대해_올바르게_동작한다() {
        // given
        WeekdayScheduleRule rule = WeekdayScheduleRule.dayOff(DayOfWeek.SUNDAY);

        // when
        String result = rule.toString();

        // then
        assertThat(result).contains("SUNDAY");
        assertThat(result).contains("휴무");
    }
}