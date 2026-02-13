package net.catsnap.CatsnapReservation.schedule.fixture;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;

/**
 * PhotographerSchedule 테스트용 Fixture
 */
public class PhotographerScheduleFixture {

    public static final Long DEFAULT_PHOTOGRAPHER_ID = 1L;

    /**
     * 기본 스케줄 생성 (모든 요일 휴무)
     */
    public static PhotographerSchedule createDefault() {
        return PhotographerSchedule.initSchedule(DEFAULT_PHOTOGRAPHER_ID);
    }

    /**
     * 지정된 photographerId로 기본 스케줄 생성
     */
    public static PhotographerSchedule createWithPhotographerId(Long photographerId) {
        return PhotographerSchedule.initSchedule(photographerId);
    }

    /**
     * 평일 근무 스케줄 생성 (월~금 9:00, 10:00, 11:00)
     */
    public static PhotographerSchedule createWeekdaySchedule(Long photographerId) {
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        AvailableStartTimes workingTimes = AvailableStartTimes.of(List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        ));

        schedule.updateWeekdayRule(DayOfWeek.MONDAY, workingTimes);
        schedule.updateWeekdayRule(DayOfWeek.TUESDAY, workingTimes);
        schedule.updateWeekdayRule(DayOfWeek.WEDNESDAY, workingTimes);
        schedule.updateWeekdayRule(DayOfWeek.THURSDAY, workingTimes);
        schedule.updateWeekdayRule(DayOfWeek.FRIDAY, workingTimes);

        return schedule;
    }
}
