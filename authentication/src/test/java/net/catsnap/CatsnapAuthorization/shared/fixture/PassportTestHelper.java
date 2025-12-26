package net.catsnap.CatsnapAuthorization.shared.fixture;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * 테스트용 Passport 헤더 설정 헬퍼 클래스
 * <p>
 * MockMvc 테스트에서 서명된 Passport 헤더를 편리하게 설정할 수 있도록 도와줍니다. 게이트웨이에서 발급하는 서명된 Passport 헤더(X-Passport)를
 * 시뮬레이션합니다.
 * </p>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @Autowired
 * private PassportTestHelper passportTestHelper;
 *
 * // 관리자 권한으로 요청
 * mockMvc.perform(passportTestHelper.withAdmin(get("/api/users")))
 *     .andExpect(status().isOk());
 *
 * // 사진작가 권한으로 요청
 * mockMvc.perform(passportTestHelper.withPhotographer(post("/api/portfolios"), 100L)
 *     .content(requestBody))
 *     .andExpect(status().isCreated());
 *
 * // 커스텀 권한으로 요청
 * mockMvc.perform(passportTestHelper.withAuthority(get("/api/profile"), 1L, CatsnapAuthority.MODEL))
 *     .andExpect(status().isOk());
 * }</pre>
 */
@TestComponent
public class PassportTestHelper {

    /**
     * PassportHandler 인스턴스 (PassportConfig에서 주입)
     */
    private final PassportHandler passportHandler;

    /**
     * Passport 헤더 키
     */
    private static final String PASSPORT_HEADER = PassportHandler.PassportKey;

    /**
     * 기본 관리자 사용자 ID
     */
    private static final Long DEFAULT_ADMIN_USER_ID = 1L;

    /**
     * 기본 익명 사용자 ID
     */
    private static final Long DEFAULT_ANONYMOUS_USER_ID = -1L;

    /**
     * 기본 Passport 유효 기간 (분)
     */
    private static final int DEFAULT_EXPIRATION_MINUTES = 30;

    /**
     * 기본 Passport 버전
     */
    private static final byte DEFAULT_PASSPORT_VERSION = 1;

    /**
     * PassportTestHelper 생성자
     * PassportConfig에서 제공하는 PassportHandler를 주입받습니다.
     *
     * @param passportHandler PassportConfig에서 생성된 PassportHandler 빈
     */
    @Autowired
    public PassportTestHelper(PassportHandler passportHandler) {
        this.passportHandler = passportHandler;
    }

    /**
     * 관리자(ADMIN) 권한으로 요청을 설정합니다.
     *
     * @param builder MockMvc 요청 빌더
     * @return 관리자 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withAdmin(MockHttpServletRequestBuilder builder) {
        return withAuthority(builder, DEFAULT_ADMIN_USER_ID, CatsnapAuthority.ADMIN);
    }

    /**
     * 관리자(ADMIN) 권한으로 요청을 설정합니다.
     *
     * @param builder MockMvc 요청 빌더
     * @param userId  사용자 ID
     * @return 관리자 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withAdmin(MockHttpServletRequestBuilder builder,
        Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.ADMIN);
    }

    /**
     * 사진작가(PHOTOGRAPHER) 권한으로 요청을 설정합니다.
     *
     * @param builder MockMvc 요청 빌더
     * @param userId  사용자 ID
     * @return 사진작가 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withPhotographer(
        MockHttpServletRequestBuilder builder, Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.PHOTOGRAPHER);
    }

    /**
     * 모델(MODEL) 권한으로 요청을 설정합니다.
     *
     * @param builder MockMvc 요청 빌더
     * @param userId  사용자 ID
     * @return 모델 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withModel(MockHttpServletRequestBuilder builder,
        Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.MODEL);
    }

    /**
     * 익명(ANONYMOUS) 사용자로 요청을 설정합니다.
     *
     * @param builder MockMvc 요청 빌더
     * @return 익명 사용자 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withAnonymous(
        MockHttpServletRequestBuilder builder) {
        return withAuthority(builder, DEFAULT_ANONYMOUS_USER_ID, CatsnapAuthority.ANONYMOUS);
    }

    /**
     * 지정된 권한으로 요청을 설정합니다.
     * <p>
     * 서명된 Passport를 생성하여 X-Passport 헤더에 추가합니다.
     * </p>
     *
     * @param builder   MockMvc 요청 빌더
     * @param userId    사용자 ID
     * @param authority 사용자 권한
     * @return Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withAuthority(
        MockHttpServletRequestBuilder builder,
        Long userId,
        CatsnapAuthority authority) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(DEFAULT_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        Passport passport = new Passport(DEFAULT_PASSPORT_VERSION, userId, authority, now, exp);
        String signedPassport = passportHandler.sign(passport);

        return builder.header(PASSPORT_HEADER, signedPassport);
    }

    /**
     * 유효하지 않은 Passport로 요청을 설정합니다.
     * <p>
     * 잘못된 서명 테스트를 위해 사용됩니다.
     * </p>
     *
     * @param builder MockMvc 요청 빌더
     * @return 유효하지 않은 Passport 헤더가 추가된 요청 빌더
     */
    public MockHttpServletRequestBuilder withInvalidPassport(
        MockHttpServletRequestBuilder builder) {
        return builder.header(PASSPORT_HEADER, "invalid-passport-string");
    }

    /**
     * 인증 헤더 없이 요청을 설정합니다.
     * <p>
     * 인증 실패 테스트를 위해 사용됩니다.
     * </p>
     *
     * @param builder MockMvc 요청 빌더
     * @return 원본 요청 빌더 (변경 없음)
     */
    public MockHttpServletRequestBuilder withoutAuth(
        MockHttpServletRequestBuilder builder) {
        return builder;
    }
}