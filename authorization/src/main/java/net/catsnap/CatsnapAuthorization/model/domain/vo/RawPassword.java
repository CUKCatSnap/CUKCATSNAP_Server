package net.catsnap.CatsnapAuthorization.model.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import net.catsnap.CatsnapAuthorization.shared.exception.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode.CommonErrorCode;

/**
 * 평문 비밀번호 값 객체 (Value Object)
 *
 * <p>사용자가 입력한 암호화되지 않은 원본 비밀번호를 표현하는 불변 객체입니다.
 *
 * <p><strong>사용 예시:</strong></p>
 * <pre>{@code
 * RawPassword raw = new RawPassword("myP@ssw0rd123");
 * EncodedPassword encoded = raw.encode(passwordEncoder);  // 암호화
 * }</pre>
 *
 * @see EncodedPassword
 * @see PasswordEncoder
 */
@Getter
public class RawPassword {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 64;

    private final String value;

    public RawPassword(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "비밀번호는 필수입니다.");
        }
        if (value.length() < MIN_LENGTH) {
            String message = String.format("비밀번호는 최소 %d자 이상이어야 합니다. 현재: %d자",
                MIN_LENGTH, value.length());
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
        if (value.length() > MAX_LENGTH) {
            String message = String.format("비밀번호는 최대 %d자 이하여야 합니다. 현재: %d자",
                MAX_LENGTH, value.length());
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    /**
     * 평문 비밀번호를 암호화하여 EncodedPassword로 변환합니다.
     *
     * @param encoder 비밀번호 암호화 인터페이스
     * @return 암호화된 비밀번호 값 객체
     */
    public EncodedPassword encode(PasswordEncoder encoder) {
        return new EncodedPassword(encoder.encode(this.value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawPassword that = (RawPassword) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "***********";  // 보안상 평문 노출 방지
    }
}