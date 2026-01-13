package net.catsnap.CatsnapReservation.schedule.domain.vo;

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

    private final AvailableStartTimes availableStartTimes;

    private WeekdayScheduleRule(AvailableStartTimes availableStartTimes) {
        if (availableStartTimes == null) {
            throw new IllegalArgumentException("예약 가능 시간은 필수입니다.");
        }

        this.availableStartTimes = availableStartTimes;
    }

    /**
     * 근무일 규칙 생성
     */
    public static WeekdayScheduleRule workingDay(AvailableStartTimes availableStartTimes) {
        return new WeekdayScheduleRule(availableStartTimes);
    }

    /**
     * 근무일 규칙 생성 (List<LocalTime>으로 편의 메서드)
     */
    public static WeekdayScheduleRule workingDay(List<LocalTime> times) {
        return new WeekdayScheduleRule(AvailableStartTimes.of(times));
    }

    /**
     * 휴무일 규칙 생성
     */
    public static WeekdayScheduleRule dayOff() {
        return new WeekdayScheduleRule(AvailableStartTimes.empty());
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
        return Objects.equals(availableStartTimes, that.availableStartTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableStartTimes);
    }

    @Override
    public String toString() {
        if (!isWorkingDay()) {
            return "WeekdayScheduleRule{휴무}";
        }
        return String.format("WeekdayScheduleRule{%s}", availableStartTimes);
    }
}
