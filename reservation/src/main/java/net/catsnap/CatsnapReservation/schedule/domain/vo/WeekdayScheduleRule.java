package net.catsnap.CatsnapReservation.schedule.domain.vo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

/**
 * 요일별 스케줄 규칙 값 객체
 * <p>
 * 특정 요일의 반복 규칙을 정의합니다. 예) 매주 월요일: [09:00, 09:30, 10:00, ...] 휴무일 여부는 availableStartTimes.isEmpty()로
 * 판단합니다.
 */
@Getter
public class WeekdayScheduleRule {

    private final DayOfWeek dayOfWeek;
    private final AvailableStartTimes availableStartTimes;

    private WeekdayScheduleRule(DayOfWeek dayOfWeek, AvailableStartTimes availableStartTimes) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("요일은 필수입니다.");
        }
        if (availableStartTimes == null) {
            throw new IllegalArgumentException("예약 가능 시간은 필수입니다.");
        }

        this.dayOfWeek = dayOfWeek;
        this.availableStartTimes = availableStartTimes;
    }

    /**
     * 근무일 규칙 생성
     */
    public static WeekdayScheduleRule workingDay(
        DayOfWeek dayOfWeek,
        AvailableStartTimes availableStartTimes
    ) {
        return new WeekdayScheduleRule(dayOfWeek, availableStartTimes);
    }

    /**
     * 근무일 규칙 생성 (List<LocalTime>으로 편의 메서드)
     */
    public static WeekdayScheduleRule workingDay(
        DayOfWeek dayOfWeek,
        List<LocalTime> times
    ) {
        return new WeekdayScheduleRule(dayOfWeek, AvailableStartTimes.of(times));
    }

    /**
     * 휴무일 규칙 생성
     */
    public static WeekdayScheduleRule dayOff(DayOfWeek dayOfWeek) {
        return new WeekdayScheduleRule(dayOfWeek, AvailableStartTimes.empty());
    }

    /**
     * 근무일 여부 (availableStartTimes가 비어있지 않으면 근무일)
     */
    public boolean isWorkingDay() {
        return !availableStartTimes.isEmpty();
    }

    /**
     * 이 규칙에 따른 예약 가능 시간 생성
     */
    public List<LocalTime> generateAvailableTimes() {
        return availableStartTimes.toList();
    }

    /**
     * 특정 시간이 유효한 예약 시작 시간인지 확인
     */
    public boolean isValidStartTime(LocalTime time) {
        if (!isWorkingDay()) {
            return false;
        }
        return availableStartTimes.contains(time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeekdayScheduleRule that = (WeekdayScheduleRule) o;
        return dayOfWeek == that.dayOfWeek
            && Objects.equals(availableStartTimes, that.availableStartTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, availableStartTimes);
    }

    @Override
    public String toString() {
        if (!isWorkingDay()) {
            return String.format("%s: 휴무", dayOfWeek);
        }
        return String.format("%s: %s", dayOfWeek, availableStartTimes);
    }
}
