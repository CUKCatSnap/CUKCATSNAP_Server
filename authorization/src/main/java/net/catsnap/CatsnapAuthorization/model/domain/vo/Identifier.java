package net.catsnap.CatsnapAuthorization.model.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.shared.exception.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode.CommonErrorCode;

/**
 * 모델 식별자 값 객체 (Value Object)
 *
 * <p>사용자를 고유하게 식별하는 ID를 표현하는 불변 객체입니다.
 */
@Getter
public class Identifier {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 20;

    private final String value;

    public Identifier(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "식별자는 필수입니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            String message = String.format("식별자는 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
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
        Identifier that = (Identifier) o;
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