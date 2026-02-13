package net.catsnap.CatsnapReservation.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import net.catsnap.CatsnapReservation.schedule.infrastructure.converter.AvailableStartTimesConverter;

/**
 * 예약 가능 시간 예외 엔티티
 * <p>
 * 특정 날짜의 예외 처리를 정의합니다. - 휴무 처리 - 특별 영업시간 설정 - 특정 시간대만 불가 처리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PhotographerSchedule photographerSchedule;

    private LocalDate targetDate;

    /**
     * 예약 가능 시작 시간 목록
     */
    @Column(columnDefinition = "jsonb")
    @Convert(converter = AvailableStartTimesConverter.class)
    private AvailableStartTimes availableTimes;

    private ScheduleOverride(
        LocalDate targetDate,
        AvailableStartTimes availableTimes
    ) {
        validateOverride(targetDate, availableTimes);

        this.targetDate = targetDate;
        this.availableTimes = availableTimes;
    }

    private void validateOverride(
        LocalDate targetDate,
        AvailableStartTimes availableTimes
    ) {
        if (targetDate == null) {
            throw new IllegalArgumentException("대상 날짜는 필수입니다.");
        }
        if (availableTimes == null) {
            throw new IllegalArgumentException("덮어쓴 시간표는 null일 수 없습니다.");
        }
    }

    /**
     * 예외 규칙 생성 (커스텀 시간 설정)
     */
    public static ScheduleOverride create(
        LocalDate targetDate,
        AvailableStartTimes availableTimes
    ) {
        return new ScheduleOverride(targetDate, availableTimes);
    }

    /**
     * 특정 날짜를 휴무로 설정
     */
    public static ScheduleOverride dayOff(LocalDate targetDate) {
        return new ScheduleOverride(targetDate, AvailableStartTimes.empty());
    }

    /**
     * 예약 가능 시간이 있는지 확인
     */
    public boolean hasAvailableTimes() {
        return availableTimes != null && !availableTimes.isEmpty();
    }

    /**
     * 예약 가능 시간 변경
     */
    public void updateAvailableTimes(AvailableStartTimes newTimes) {
        if (newTimes == null) {
            throw new IllegalArgumentException("예약 가능 시간은 null일 수 없습니다.");
        }
        this.availableTimes = newTimes;
    }

    /**
     * 휴무일로 변경
     */
    public void changeToDayOff() {
        this.availableTimes = AvailableStartTimes.empty();
    }

    /**
     * 편의 메서드: 작가 ID 조회
     */
    public Long getPhotographerId() {
        return photographerSchedule != null ? photographerSchedule.getPhotographerId() : null;
    }

    /**
     * 양방향 연관관계 설정 (package-private)
     */
    void setPhotographerSchedule(PhotographerSchedule photographerSchedule) {
        this.photographerSchedule = photographerSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleOverride that = (ScheduleOverride) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "ScheduleOverride{id=%d, targetDate=%s}",
            id, targetDate);
    }
}
