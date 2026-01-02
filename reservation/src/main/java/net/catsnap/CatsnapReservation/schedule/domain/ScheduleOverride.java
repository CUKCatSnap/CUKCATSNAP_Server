package net.catsnap.CatsnapReservation.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long photographerId;

    private LocalDate targetDate;

    /**
     * 예약 가능 시작 시간 목록
     */
    @Column(columnDefinition = "jsonb")
    @Convert(converter = AvailableStartTimesConverter.class)
    private AvailableStartTimes availableTimes;

    private ScheduleOverride(
        Long photographerId,
        LocalDate targetDate,
        AvailableStartTimes availableTimes
    ) {
        validateOverride(photographerId, targetDate, availableTimes);

        this.photographerId = photographerId;
        this.targetDate = targetDate;
        this.availableTimes = availableTimes;
    }

    private void validateOverride(
        Long photographerId,
        LocalDate targetDate,
        AvailableStartTimes availableTimes
    ) {
        if (photographerId == null) {
            throw new IllegalArgumentException("작가 ID는 필수입니다.");
        }
        if (targetDate == null) {
            throw new IllegalArgumentException("대상 날짜는 필수입니다.");
        }
        if (availableTimes == null) {
            throw new IllegalArgumentException("덮어쓴 시간표는 null일 수 없습니다.");
        }
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
            "AvailabilityOverride{id=%d, photographerId=%d, targetDate=%s}",
            id, photographerId, targetDate);
    }
}
