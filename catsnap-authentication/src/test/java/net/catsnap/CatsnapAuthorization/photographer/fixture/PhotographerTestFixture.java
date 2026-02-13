package net.catsnap.CatsnapAuthorization.photographer.fixture;

import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;

/**
 * Photographer 엔티티 테스트 픽스처
 *
 * <p>테스트에서 Photographer 엔티티를 쉽게 생성하기 위한 헬퍼 클래스입니다.
 * 빌더 패턴을 사용하여 필요한 값만 커스터마이징하고 나머지는 기본값을 사용할 수 있습니다.</p>
 *
 * <p>PasswordEncoder는 내부적으로 관리되며, 기본적으로 테스트용 구현을 사용합니다.
 * 필요시 {@link #passwordEncoder(PasswordEncoder)} 메서드로 커스텀 인코더를 설정할 수 있습니다.</p>
 *
 */
public class PhotographerTestFixture {

    /**
     * 테스트용 PasswordEncoder 구현입니다. 실제 암호화 없이 간단히 접두사만 추가합니다.
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

    private static final String DEFAULT_IDENTIFIER = "testphotographer";
    private static final String DEFAULT_PASSWORD = "password1234";
    private static final String DEFAULT_NAME = "홍길동";
    private static final String DEFAULT_PHONE_NUMBER = "010-1234-5678";

    private String identifier = DEFAULT_IDENTIFIER;
    private String password = DEFAULT_PASSWORD;
    private String name = DEFAULT_NAME;
    private String phoneNumber = DEFAULT_PHONE_NUMBER;
    private PasswordEncoder passwordEncoder = new TestPasswordEncoder();

    private PhotographerTestFixture() {
    }

    /**
     * 빌더 생성
     */
    public static PhotographerTestFixture builder() {
        return new PhotographerTestFixture();
    }

    /**
     * 기본값으로 Photographer 생성 (테스트용 PasswordEncoder 사용)
     */
    public static Photographer create() {
        return builder().build();
    }

    /**
     * 커스텀 PasswordEncoder를 사용하여 기본 설정의 Photographer 생성
     */
    public static Photographer create(PasswordEncoder passwordEncoder) {
        return builder().passwordEncoder(passwordEncoder).build();
    }

    public PhotographerTestFixture identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public PhotographerTestFixture password(String password) {
        this.password = password;
        return this;
    }

    public PhotographerTestFixture name(String name) {
        this.name = name;
        return this;
    }

    public PhotographerTestFixture phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PhotographerTestFixture passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    /**
     * 설정된 값으로 Photographer 엔티티 생성
     *
     * <p>Aggregate Root인 Photographer가 내부에서 VO를 직접 생성합니다 (DDD 패턴).
     * 원시 타입만 전달하면 Photographer가 검증과 VO 생성을 담당합니다.</p>
     */
    public Photographer build() {
        return Photographer.signUp(
            identifier,
            password,
            name,
            phoneNumber,
            this.passwordEncoder
        );
    }
}