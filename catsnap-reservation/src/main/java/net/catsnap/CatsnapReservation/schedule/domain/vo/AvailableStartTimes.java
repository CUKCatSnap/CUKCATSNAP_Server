package net.catsnap.CatsnapReservation.schedule.domain.vo;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 예약 가능 시작 시간 목록 값 객체
 * <p>
 * 불변 객체로, 예약 가능한 시작 시간들을 관리합니다. 자동으로 정렬되고 중복이 제거됩니다.
 */
public class AvailableStartTimes {

    private final List<LocalTime> times;

    private AvailableStartTimes(List<LocalTime> times) {
        if (times == null) {
            this.times = Collections.emptyList();
        } else {
            // 정렬 + 중복 제거
            this.times = times.stream()
                .distinct()
                .sorted()
                .toList();
        }
    }

    /**
     * 시간 목록으로 생성
     */
    public static AvailableStartTimes of(List<LocalTime> times) {
        if (times == null) {
            throw new IllegalArgumentException("예약 가능 시간은 null일 수 없습니다.");
        }
        return new AvailableStartTimes(times);
    }

    /**
     * 빈 시간 목록 (휴무일용)
     */
    public static AvailableStartTimes empty() {
        return new AvailableStartTimes(null);
    }

    /**
     * 특정 시간이 예약 가능한 시작 시간인지 확인
     */
    public boolean contains(LocalTime time) {
        return times.contains(time);
    }

    /**
     * 비어있는지 확인
     */
    public boolean isEmpty() {
        return times.isEmpty();
    }

    /**
     * 시간 개수
     */
    public int size() {
        return times.size();
    }

    /**
     * 불변 리스트로 반환
     */
    public List<LocalTime> toList() {
        return Collections.unmodifiableList(times);
    }

    /**
     * 첫 번째 시간 반환
     */
    public LocalTime first() {
        if (times.isEmpty()) {
            throw new IllegalStateException("예약 가능 시간이 없습니다.");
        }
        return times.get(0);
    }

    /**
     * 마지막 시간 반환
     */
    public LocalTime last() {
        if (times.isEmpty()) {
            throw new IllegalStateException("예약 가능 시간이 없습니다.");
        }
        return times.get(times.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvailableStartTimes that = (AvailableStartTimes) o;
        return Objects.equals(times, that.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(times);
    }

    @Override
    public String toString() {
        if (times.isEmpty()) {
            return "AvailableStartTimes{empty}";
        }
        return String.format("AvailableStartTimes{%s ~ %s, count=%d}",
            first(), last(), size());
    }
}