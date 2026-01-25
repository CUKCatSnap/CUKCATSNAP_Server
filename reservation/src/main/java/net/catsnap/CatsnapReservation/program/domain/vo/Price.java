package net.catsnap.CatsnapReservation.program.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 프로그램 가격 값 객체 (Value Object)
 *
 * <p>프로그램의 가격(원)을 표현하는 불변 객체입니다.
 */
@Getter
public class Price {

    private static final long MIN_VALUE = 0L;

    private final Long value;

    public Price(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long value) {
        if (value == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "가격은 필수입니다.");
        }
        if (value < MIN_VALUE) {
            String message = String.format("가격은 %d원 이상이어야 합니다. 현재: %d원", MIN_VALUE, value);
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    public boolean isFree() {
        return value == 0L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
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
