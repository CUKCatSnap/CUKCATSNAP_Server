package net.catsnap.CatsnapReservation.program.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 프로그램 설명 값 객체 (Value Object)
 *
 * <p>프로그램의 간단 설명을 표현하는 불변 객체입니다.
 * null을 허용하며, 값이 있는 경우 최대 500자까지 허용합니다.
 */
@Getter
public class Description {

    private static final int MAX_LENGTH = 500;

    private final String value;

    public Description(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            String message = String.format("프로그램 설명은 %d자 이하여야 합니다. 현재: %d자",
                MAX_LENGTH, value.length());
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
        Description that = (Description) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? "" : value;
    }
}
