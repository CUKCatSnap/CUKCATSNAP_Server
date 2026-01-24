package net.catsnap.CatsnapReservation.shared.fixture;

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
 * MockMvc 테스트에서 서명된 Passport 헤더를 편리하게 설정할 수 있도록 도와줍니다.
 * 게이트웨이에서 발급하는 서명된 Passport 헤더(X-Passport)를 시뮬레이션합니다.
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

    private final PassportHandler passportHandler;

    private static final String PASSPORT_HEADER = PassportHandler.PASSPORT_KEY;
    private static final Long DEFAULT_ADMIN_USER_ID = 1L;
    private static final Long DEFAULT_ANONYMOUS_USER_ID = -1L;
    private static final int DEFAULT_EXPIRATION_MINUTES = 30;
    private static final byte DEFAULT_PASSPORT_VERSION = 1;

    @Autowired
    public PassportTestHelper(PassportHandler passportHandler) {
        this.passportHandler = passportHandler;
    }

    /**
     * 관리자(ADMIN) 권한으로 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withAdmin(MockHttpServletRequestBuilder builder) {
        return withAuthority(builder, DEFAULT_ADMIN_USER_ID, CatsnapAuthority.ADMIN);
    }

    /**
     * 관리자(ADMIN) 권한으로 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withAdmin(MockHttpServletRequestBuilder builder,
        Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.ADMIN);
    }

    /**
     * 사진작가(PHOTOGRAPHER) 권한으로 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withPhotographer(
        MockHttpServletRequestBuilder builder, Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.PHOTOGRAPHER);
    }

    /**
     * 모델(MODEL) 권한으로 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withModel(MockHttpServletRequestBuilder builder,
        Long userId) {
        return withAuthority(builder, userId, CatsnapAuthority.MODEL);
    }

    /**
     * 익명(ANONYMOUS) 사용자로 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withAnonymous(
        MockHttpServletRequestBuilder builder) {
        return withAuthority(builder, DEFAULT_ANONYMOUS_USER_ID, CatsnapAuthority.ANONYMOUS);
    }

    /**
     * 지정된 권한으로 요청을 설정합니다.
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
     */
    public MockHttpServletRequestBuilder withInvalidPassport(
        MockHttpServletRequestBuilder builder) {
        return builder.header(PASSPORT_HEADER, "invalid-passport-string");
    }

    /**
     * 인증 헤더 없이 요청을 설정합니다.
     */
    public MockHttpServletRequestBuilder withoutAuth(
        MockHttpServletRequestBuilder builder) {
        return builder;
    }
}
