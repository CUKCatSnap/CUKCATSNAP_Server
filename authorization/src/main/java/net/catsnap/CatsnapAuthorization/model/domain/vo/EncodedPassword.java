package net.catsnap.CatsnapAuthorization.model.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;

/**
 * 암호화된 비밀번호 값 객체 (Value Object)
 *
 * <p>암호화되어 데이터베이스에 저장되는 비밀번호를 표현하는 불변 객체입니다.
 * <p>
 * 생성 (RawPassword로부터 변환) RawPassword raw = new RawPassword("myPassword123"); EncodedPassword
 * encoded = raw.encode(passwordEncoder);
 * <p>
 * <p>
 * 검증 RawPassword inputPassword = new RawPassword("myPassword123"); boolean isMatch =
 * encoded.matches(inputPassword, passwordEncoder);  // true
 * </p>
 *
 * @see RawPassword
 * @see PasswordEncoder
 */
@Getter
public class EncodedPassword {

    private final String value;

    /**
     * 암호화된 비밀번호로 EncodedPassword를 생성합니다.
     *
     * @param encodedValue 이미 암호화된 비밀번호 문자열
     */
    public EncodedPassword(String encodedValue) {
        if (encodedValue == null || encodedValue.isBlank()) {
            throw new IllegalArgumentException("암호화된 비밀번호는 필수입니다.");
        }
        this.value = encodedValue;
    }

    /**
     * 입력된 평문 비밀번호가 이 암호화된 비밀번호와 일치하는지 확인합니다.
     *
     * @param rawPassword 사용자가 입력한 평문 비밀번호
     * @param encoder     비밀번호 검증을 위한 인터페이스
     * @return 비밀번호가 일치하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean matches(RawPassword rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword.getValue(), this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EncodedPassword that = (EncodedPassword) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "***********";  // 보안상 암호화된 값도 노출 방지
    }
}
