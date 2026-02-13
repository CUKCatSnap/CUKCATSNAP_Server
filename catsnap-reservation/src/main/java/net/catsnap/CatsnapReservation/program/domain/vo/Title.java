package net.catsnap.CatsnapReservation.program.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 프로그램 제목 값 객체 (Value Object)
 *
 * <p>프로그램의 제목을 표현하는 불변 객체입니다.
 */
@Getter
public class Title {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    private final String value;

    public Title(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "프로그램 제목은 필수입니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            String message = String.format("프로그램 제목은 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
                MIN_LENGTH, MAX_LENGTH, value.length());
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
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
        Title title = (Title) o;
        return Objects.equals(value, title.value);
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
