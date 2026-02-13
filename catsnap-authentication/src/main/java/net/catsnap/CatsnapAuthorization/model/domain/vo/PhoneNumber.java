package net.catsnap.CatsnapAuthorization.model.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;

/**
 * 전화번호 값 객체 (Value Object)
 *
 * <p>사용자의 연락처 정보를 표현하는 불변 객체입니다.
 */
@Getter
public class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null) {
            String message = "전화번호는 필수입니다.";
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }

        if (!value.matches("^010-\\d{4}-\\d{4}$")) {
            String message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.";
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
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "***-****-****";
    }
}