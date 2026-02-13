package net.catsnap.CatsnapReservation.reservation.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 예약 시간대 값 객체 (Value Object)
 *
 * <p>예약 시작 시각과 종료 시각을 함께 관리하는 복합 불변 객체입니다.</p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTimeSlot {

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    public ReservationTimeSlot(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validate(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void validate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "시작 시각은 필수입니다.");
        }
        if (endDateTime == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "종료 시각은 필수입니다.");
        }
        if (!startDateTime.isBefore(endDateTime)) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "시작 시각은 종료 시각보다 빨라야 합니다. 시작: " + startDateTime + ", 종료: " + endDateTime);
        }
    }

    /**
     * 다른 시간대와 겹치는지 확인
     *
     * @param other 비교 대상 시간대
     * @return 시간 범위가 겹치면 true
     */
    public boolean overlaps(ReservationTimeSlot other) {
        if (other == null) {
            return false;
        }
        return this.startDateTime.isBefore(other.endDateTime)
            && other.startDateTime.isBefore(this.endDateTime);
    }

    public boolean contains(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return !dateTime.isBefore(startDateTime) && dateTime.isBefore(endDateTime);
    }

    public LocalDate getReservationDate() {
        return startDateTime.toLocalDate();
    }

    public Duration duration() {
        return Duration.between(startDateTime, endDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTimeSlot that = (ReservationTimeSlot) o;
        return Objects.equals(startDateTime, that.startDateTime)
            && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime);
    }

    @Override
    public String toString() {
        return String.format("%s ~ %s", startDateTime, endDateTime);
    }
}