package net.catsnap.CatsnapReservation.reservation.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 취소 사유 값 객체 (Value Object)
 *
 * <p>예약 취소 사유를 표현하는 불변 객체입니다. nullable을 허용합니다.</p>
 */
@Getter
public class CancelReason {

    private static final int MAX_LENGTH = 300;

    private final String value;

    public CancelReason(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            String message = String.format("취소 사유는 %d자 이하여야 합니다. 현재: %d자", MAX_LENGTH, value.length());
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    public boolean isEmpty() {
        return value == null || value.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CancelReason that = (CancelReason) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}
