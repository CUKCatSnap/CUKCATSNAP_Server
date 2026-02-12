package net.catsnap.CatsnapReservation.reservation.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 금액 값 객체 (Value Object)
 *
 * <p>예약 금액(원)을 표현하는 불변 객체입니다.</p>
 */
@Getter
public class Money {

    private static final long MIN_VALUE = 0L;

    private final Long value;

    public Money(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long value) {
        if (value == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "금액은 필수입니다.");
        }
        if (value < MIN_VALUE) {
            String message = String.format("금액은 %d원 이상이어야 합니다. 현재: %d원", MIN_VALUE, value);
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    public boolean isFree() {
        return value == 0L;
    }

    public Money add(Money other) {
        if (other == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "더할 금액은 필수입니다.");
        }
        return new Money(this.value + other.value);
    }

    public Money subtract(Money other) {
        if (other == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "뺄 금액은 필수입니다.");
        }
        return new Money(this.value - other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "원";
    }
}
