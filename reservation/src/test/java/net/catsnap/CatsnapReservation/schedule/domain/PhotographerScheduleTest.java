package net.catsnap.CatsnapReservation.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import net.catsnap.CatsnapReservation.schedule.domain.vo.WeekdayScheduleRule;
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
            WeekdayScheduleRule rule = schedule.getWeekdayRules().get(day);
            assertThat(rule).isNotNull();
            assertThat(rule.getDayOfWeek()).isEqualTo(day);
            assertThat(rule.isWorkingDay()).isFalse();
            assertThat(rule.getAvailableStartTimes().isEmpty()).isTrue();
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
}