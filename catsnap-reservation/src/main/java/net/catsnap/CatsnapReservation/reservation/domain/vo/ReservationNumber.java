package net.catsnap.CatsnapReservation.reservation.domain.vo;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 외부 노출용 예약 번호 값 객체 (Value Object)
 *
 * <p>UUID 형식의 예약 번호를 표현하는 불변 객체입니다.</p>
 */
@Getter
public class ReservationNumber {

    private final String value;

    public ReservationNumber(String value) {
        validate(value);
        this.value = value;
    }

    public static ReservationNumber generate() {
        return new ReservationNumber(UUID.randomUUID().toString());
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "예약 번호는 필수입니다.");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "예약 번호는 UUID 형식이어야 합니다. 현재: " + value);
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
        ReservationNumber that = (ReservationNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
