package net.catsnap.CatsnapAuthorization.photographer.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;

/**
 * 이름 값 객체 (Value Object)
 *
 * <p>작가의 실명을 표현하는 불변 객체입니다.
 */
@Getter
public class Name {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;

    private final String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "이름은 필수입니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            String message = String.format("이름은 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
                MIN_LENGTH, MAX_LENGTH, value.length());
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
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
        Name name = (Name) o;
        return Objects.equals(value, name.value);
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