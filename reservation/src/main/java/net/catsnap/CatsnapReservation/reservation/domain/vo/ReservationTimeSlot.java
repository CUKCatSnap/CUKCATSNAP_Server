package net.catsnap.CatsnapReservation.reservation.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 예약 시간대 값 객체 (Value Object)
 *
 * <p>예약 날짜, 시작 시간, 종료 시간을 함께 관리하는 복합 불변 객체입니다.</p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTimeSlot {

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public ReservationTimeSlot(LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        validate(reservationDate, startTime, endTime);
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validate(LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        if (reservationDate == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "예약 날짜는 필수입니다.");
        }
        if (startTime == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "시작 시간은 필수입니다.");
        }
        if (endTime == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "종료 시간은 필수입니다.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "시작 시간은 종료 시간보다 빨라야 합니다. 시작: " + startTime + ", 종료: " + endTime);
        }
    }

    public boolean contains(LocalTime time) {
        if (time == null) {
            return false;
        }
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    public Duration duration() {
        return Duration.between(startTime, endTime);
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
        return Objects.equals(reservationDate, that.reservationDate)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationDate, startTime, endTime);
    }

    @Override
    public String toString() {
        return String.format("%s %s~%s", reservationDate, startTime, endTime);
    }
}
