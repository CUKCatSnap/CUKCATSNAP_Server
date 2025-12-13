package net.catsnap.CatsnapAuthorization.model.fixture;

import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Nickname;
import net.catsnap.CatsnapAuthorization.model.domain.vo.PhoneNumber;
import net.catsnap.CatsnapAuthorization.model.domain.vo.RawPassword;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;

/**
 * Model 엔티티 테스트 픽스처
 *
 * <p>테스트에서 Model 엔티티를 쉽게 생성하기 위한 헬퍼 클래스입니다.
 * 빌더 패턴을 사용하여 필요한 값만 커스터마이징하고 나머지는 기본값을 사용할 수 있습니다.</p>
 *
 * <p>PasswordEncoder는 내부적으로 관리되며, 기본적으로 테스트용 구현을 사용합니다.
 * 필요시 {@link #passwordEncoder(PasswordEncoder)} 메서드로 커스텀 인코더를 설정할 수 있습니다.</p>
 *
 */
public class ModelTestFixture {

    /**
     * 테스트용 PasswordEncoder 구현 실제 암호화 없이 간단히 접두사만 추가합니다.
     */
    private static class TestPasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(String rawPassword) {
            return "{test}" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return encodedPassword.equals("{test}" + rawPassword);
        }
    }

    private static final String DEFAULT_IDENTIFIER = "testuser";
    private static final String DEFAULT_PASSWORD = "password1234";
    private static final String DEFAULT_NICKNAME = "테스터";
    private static final String DEFAULT_PHONE_NUMBER = "010-1234-5678";
    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.of(1990, 1, 1);

    private String identifier = DEFAULT_IDENTIFIER;
    private String password = DEFAULT_PASSWORD;
    private String nickname = DEFAULT_NICKNAME;
    private String phoneNumber = DEFAULT_PHONE_NUMBER;
    private LocalDate birthday = DEFAULT_BIRTHDAY;
    private PasswordEncoder passwordEncoder = new TestPasswordEncoder();

    private ModelTestFixture() {
    }

    /**
     * 빌더 생성
     */
    public static ModelTestFixture builder() {
        return new ModelTestFixture();
    }

    /**
     * 기본값으로 Model 생성 (테스트용 PasswordEncoder 사용)
     */
    public static Model create() {
        return builder().build();
    }

    /**
     * 기본값으로 Model 생성 (커스텀 PasswordEncoder 사용)
     */
    public static Model create(PasswordEncoder passwordEncoder) {
        return builder().passwordEncoder(passwordEncoder).build();
    }

    public ModelTestFixture identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public ModelTestFixture password(String password) {
        this.password = password;
        return this;
    }

    public ModelTestFixture nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ModelTestFixture phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ModelTestFixture birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public ModelTestFixture passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    /**
     * 설정된 값으로 Model 엔티티 생성 (내부 PasswordEncoder 사용)
     */
    public Model build() {
        return Model.signUp(
            new Identifier(identifier),
            new RawPassword(password),
            new Nickname(nickname),
            birthday,
            new PhoneNumber(phoneNumber),
            this.passwordEncoder
        );
    }
}