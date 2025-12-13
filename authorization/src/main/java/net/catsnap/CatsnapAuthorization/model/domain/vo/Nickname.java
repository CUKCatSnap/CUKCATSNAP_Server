package net.catsnap.CatsnapAuthorization.model.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;

/**
 * 닉네임 값 객체 (Value Object)
 *
 * <p>사용자의 표시 이름을 표현하는 불변 객체입니다.
 */
@Getter
public class Nickname {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 20;

    private final String value;

    public Nickname(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "닉네임은 필수입니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            String message = String.format("닉네임은 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
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
        Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
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