package net.catsnap.CatsnapReservation.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PhotographerSchedule 도메인 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerScheduleTest {

    @Test
    void photographerId로_스케줄_초기화에_성공한다() {
        // given
        Long photographerId = 1L;

        // when
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);

        // then
        assertThat(schedule.getPhotographerId()).isEqualTo(photographerId);
        assertThat(schedule.getWeekdayRules()).isNotNull();
        assertThat(schedule.getWeekdayRules()).hasSize(7);
        assertThat(schedule.getOverrides()).isEmpty();
    }

    @Test
    void 기본_요일_규칙이_모든_요일_휴무로_초기화된다() {
        // given
        Long photographerId = 1L;

        // when
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);

        // then
        for (DayOfWeek day : DayOfWeek.values()) {
            AvailableStartTimes times = schedule.getWeekdayRules().get(day);
            assertThat(times).isNotNull();
            assertThat(times.isEmpty()).isTrue();
        }
    }

    @Test
    void photographerId가_null이면_예외가_발생한다() {
        // given
        Long photographerId = null;

        // when & then
        assertThatThrownBy(() -> PhotographerSchedule.initSchedule(photographerId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작가 ID는 필수입니다.");
    }

    @Test
    void 예외_규칙을_추가할_수_있다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);

        // when
        schedule.addOverride(override);

        // then
        assertThat(schedule.getOverrides()).hasSize(1);
        assertThat(schedule.getOverrides().get(0).getTargetDate()).isEqualTo(targetDate);
    }

    @Test
    void 예외_규칙이_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);

        // when & then
        assertThatThrownBy(() -> schedule.addOverride(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예외 규칙은 필수입니다.");
    }

    @Test
    void 중복된_날짜의_예외_규칙을_추가하면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        ScheduleOverride override1 = ScheduleOverride.dayOff(targetDate);
        ScheduleOverride override2 = ScheduleOverride.dayOff(targetDate);
        schedule.addOverride(override1);

        // when & then
        assertThatThrownBy(() -> schedule.addOverride(override2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("해당 날짜");
    }

    @Test
    void 예외_규칙을_제거할_수_있다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);
        schedule.addOverride(override);

        // when
        schedule.removeOverride(override);

        // then
        assertThat(schedule.getOverrides()).isEmpty();
    }

    @Test
    void 요일_규칙을_업데이트할_수_있다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        DayOfWeek monday = DayOfWeek.MONDAY;
        LocalTime time1 = LocalTime.of(9, 0);
        LocalTime time2 = LocalTime.of(10, 0);
        AvailableStartTimes newTimes = AvailableStartTimes.of(java.util.List.of(time1, time2));

        // when
        schedule.updateWeekdayRule(monday, newTimes);

        // then
        assertThat(schedule.getWeekdayRules().get(monday)).isEqualTo(newTimes);
        assertThat(schedule.getWeekdayRules().get(monday).isEmpty()).isFalse();
    }

    @Test
    void 요일_규칙_업데이트_시_요일이_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        AvailableStartTimes times = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(() -> schedule.updateWeekdayRule(null, times))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요일은 필수입니다.");
    }

    @Test
    void 요일_규칙_업데이트_시_시간이_null이면_예외가_발생한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        DayOfWeek monday = DayOfWeek.MONDAY;

        // when & then
        assertThatThrownBy(() -> schedule.updateWeekdayRule(monday, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 시간은 필수입니다.");
    }

    @Test
    void 예약_가능한_시간이면_ensureAvailable이_성공한다() {
        // given
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(1L);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();
        LocalTime availableTime = LocalTime.of(10, 0);
        schedule.updateWeekdayRule(dayOfWeek, AvailableStartTimes.of(java.util.List.of(availableTime)));

        // when & then
        assertThatCode(() -> schedule.ensureAvailable(targetDate, availableTime))
            .doesNotThrowAnyException();
    }

    @Test
    void 예약_불가능한_시간이면_ensureAvailable에서_예외가_발생한다() {
        // given
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(1L);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();
        schedule.updateWeekdayRule(dayOfWeek, AvailableStartTimes.of(java.util.List.of(LocalTime.of(10, 0))));

        // when & then
        assertThatThrownBy(() -> schedule.ensureAvailable(targetDate, LocalTime.of(14, 0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("해당 시간대는 예약할 수 없습니다");
    }

    @Test
    void 과거_날짜는_예약_불가능하다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate pastDate = LocalDate.now().minusDays(1);

        // when
        boolean available = schedule.isAvailableAt(pastDate);

        // then
        assertThat(available).isFalse();
    }

    @Test
    void 예외_규칙이_있으면_예외_규칙을_우선한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

        // 기본 규칙은 근무일로 설정
        LocalTime time = LocalTime.of(9, 0);
        AvailableStartTimes workingTimes = AvailableStartTimes.of(java.util.List.of(time));
        schedule.updateWeekdayRule(dayOfWeek, workingTimes);

        // 예외 규칙은 휴무로 설정
        ScheduleOverride override = ScheduleOverride.dayOff(targetDate);
        schedule.addOverride(override);

        // when
        boolean available = schedule.isAvailableAt(targetDate);

        // then
        assertThat(available).isFalse();
    }

    @Test
    void 예외_규칙이_없으면_기본_요일_규칙을_따른다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

        // 기본 규칙을 근무일로 설정
        LocalTime time = LocalTime.of(9, 0);
        AvailableStartTimes workingTimes = AvailableStartTimes.of(java.util.List.of(time));
        schedule.updateWeekdayRule(dayOfWeek, workingTimes);

        // when
        boolean available = schedule.isAvailableAt(targetDate);

        // then
        assertThat(available).isTrue();
    }

    @Test
    void 기본_규칙이_휴무면_예약_불가능하다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);

        // 기본 규칙은 모두 휴무 (초기화 시 기본값)

        // when
        boolean available = schedule.isAvailableAt(targetDate);

        // then
        assertThat(available).isFalse();
    }

    @Test
    void getAvailableStartTimesAt은_예외_규칙이_있으면_예외_규칙의_시간을_반환한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

        AvailableStartTimes weekdayTimes = AvailableStartTimes.of(java.util.List.of(LocalTime.of(9, 0)));
        schedule.updateWeekdayRule(dayOfWeek, weekdayTimes);

        AvailableStartTimes overrideTimes = AvailableStartTimes.of(
            java.util.List.of(LocalTime.of(14, 0), LocalTime.of(15, 0)));
        ScheduleOverride override = ScheduleOverride.create(targetDate, overrideTimes);
        schedule.addOverride(override);

        // when
        AvailableStartTimes result = schedule.getAvailableStartTimesAt(targetDate);

        // then
        assertThat(result).isEqualTo(overrideTimes);
    }

    @Test
    void getAvailableStartTimesAt은_예외_규칙이_없으면_요일_규칙의_시간을_반환한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();

        AvailableStartTimes weekdayTimes = AvailableStartTimes.of(
            java.util.List.of(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        schedule.updateWeekdayRule(dayOfWeek, weekdayTimes);

        // when
        AvailableStartTimes result = schedule.getAvailableStartTimesAt(targetDate);

        // then
        assertThat(result).isEqualTo(weekdayTimes);
    }

    @Test
    void getAvailableStartTimesAt은_규칙이_없으면_빈_목록을_반환한다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        LocalDate targetDate = LocalDate.now().plusDays(1);

        // when
        AvailableStartTimes result = schedule.getAvailableStartTimesAt(targetDate);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 과거_예외_규칙은_자동으로_정리된다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);

        // 과거 예외 추가 (리플렉션이나 다른 방법으로 추가)
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ScheduleOverride pastOverride = ScheduleOverride.dayOff(pastDate);
        ScheduleOverride futureOverride = ScheduleOverride.dayOff(futureDate);

        // 리플렉션을 사용하지 않고 직접 추가
        schedule.getOverrides().add(pastOverride);
        schedule.getOverrides().add(futureOverride);

        // when - 수정 작업 수행 시 과거 데이터 정리됨
        schedule.updateWeekdayRule(DayOfWeek.MONDAY, AvailableStartTimes.empty());

        // then
        assertThat(schedule.getOverrides()).hasSize(1);
        assertThat(schedule.getOverrides().get(0).getTargetDate()).isEqualTo(futureDate);
    }
}