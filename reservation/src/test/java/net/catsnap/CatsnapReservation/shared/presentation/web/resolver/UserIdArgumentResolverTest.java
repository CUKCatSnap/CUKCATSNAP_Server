package net.catsnap.CatsnapReservation.shared.presentation.web.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationException;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import net.catsnap.shared.passport.infrastructure.BinaryPassportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

@DisplayName("UserIdArgumentResolver 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserIdArgumentResolverTest {

    private static final String TEST_SECRET_KEY = "test-secret-key-for-passport-signing-32bytes!!";

    private PassportHandler passportHandler;
    private UserIdArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        passportHandler = new BinaryPassportHandler(TEST_SECRET_KEY);
        resolver = new UserIdArgumentResolver(passportHandler);
    }

    @Nested
    class supportsParameter_테스트 {

        @Test
        void UserId_어노테이션이_있으면_true를_반환한다() throws NoSuchMethodException {
            // given
            MethodParameter parameter = new MethodParameter(
                TestController.class.getMethod("methodWithUserId", Long.class), 0);

            // when
            boolean result = resolver.supportsParameter(parameter);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void UserId_어노테이션이_없으면_false를_반환한다() throws NoSuchMethodException {
            // given
            MethodParameter parameter = new MethodParameter(
                TestController.class.getMethod("methodWithoutUserId", Long.class), 0);

            // when
            boolean result = resolver.supportsParameter(parameter);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class resolveArgument_테스트 {

        @Test
        void 유효한_Passport에서_userId를_추출한다() throws Exception {
            // given
            Long expectedUserId = 123L;
            String signedPassport = createSignedPassport(expectedUserId, CatsnapAuthority.PHOTOGRAPHER);

            NativeWebRequest webRequest = mock(NativeWebRequest.class);
            HttpServletRequest httpRequest = mock(HttpServletRequest.class);
            when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpRequest);
            when(httpRequest.getHeader(PassportHandler.PASSPORT_KEY)).thenReturn(signedPassport);

            // when
            Object result = resolver.resolveArgument(null, null, webRequest, null);

            // then
            assertThat(result).isEqualTo(expectedUserId);
        }

        @Test
        void Passport_헤더가_없으면_UNAUTHORIZED_예외가_발생한다() {
            // given
            NativeWebRequest webRequest = mock(NativeWebRequest.class);
            HttpServletRequest httpRequest = mock(HttpServletRequest.class);
            when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpRequest);
            when(httpRequest.getHeader(PassportHandler.PASSPORT_KEY)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
                .isInstanceOf(PresentationException.class)
                .extracting("resultCode")
                .isEqualTo(PresentationErrorCode.UNAUTHORIZED);
        }

        @Test
        void 빈_Passport_헤더면_UNAUTHORIZED_예외가_발생한다() {
            // given
            NativeWebRequest webRequest = mock(NativeWebRequest.class);
            HttpServletRequest httpRequest = mock(HttpServletRequest.class);
            when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpRequest);
            when(httpRequest.getHeader(PassportHandler.PASSPORT_KEY)).thenReturn("");

            // when & then
            assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
                .isInstanceOf(PresentationException.class)
                .extracting("resultCode")
                .isEqualTo(PresentationErrorCode.UNAUTHORIZED);
        }

        @Test
        void 유효하지_않은_Passport면_INVALID_PASSPORT_예외가_발생한다() {
            // given
            NativeWebRequest webRequest = mock(NativeWebRequest.class);
            HttpServletRequest httpRequest = mock(HttpServletRequest.class);
            when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(httpRequest);
            when(httpRequest.getHeader(PassportHandler.PASSPORT_KEY)).thenReturn("invalid-passport");

            // when & then
            assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
                .isInstanceOf(PresentationException.class)
                .extracting("resultCode")
                .isEqualTo(PresentationErrorCode.INVALID_PASSPORT);
        }
    }

    private String createSignedPassport(Long userId, CatsnapAuthority authority) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, userId, authority, now, exp);
        return passportHandler.sign(passport);
    }

    // 테스트용 컨트롤러
    static class TestController {

        public void methodWithUserId(@UserId Long userId) {
        }

        public void methodWithoutUserId(Long userId) {
        }
    }
}
