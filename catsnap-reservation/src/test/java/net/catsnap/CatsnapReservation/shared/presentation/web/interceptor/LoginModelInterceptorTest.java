package net.catsnap.CatsnapReservation.shared.presentation.web.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationException;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginModel;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

@DisplayName("LoginModelInterceptor 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LoginModelInterceptorTest {

    private LoginModelInterceptor loginModelInterceptor;
    private PassportHandler passportHandler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        passportHandler = mock(PassportHandler.class);
        loginModelInterceptor = new LoginModelInterceptor(passportHandler);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void LoginModel_어노테이션을_붙이지_않으면_통과한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("methodWithoutAnnotation");

        // when & then
        assertTrue(loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void MODEL_권한이_있으면_통과한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 1L, CatsnapAuthority.MODEL, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertTrue(loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void ADMIN_권한이_있으면_통과한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 2L, CatsnapAuthority.ADMIN, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertTrue(loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void PHOTOGRAPHER_권한이면_FORBIDDEN_예외가_발생한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 3L, CatsnapAuthority.PHOTOGRAPHER, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertThatThrownBy(() -> loginModelInterceptor.preHandle(request, response, handler))
            .isInstanceOf(PresentationException.class)
            .satisfies(e -> {
                PresentationException ex = (PresentationException) e;
                assertThat(ex.getResultCode()).isEqualTo(PresentationErrorCode.FORBIDDEN);
            });
    }

    @Test
    void 인증_헤더가_없으면_UNAUTHORIZED_예외가_발생한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertThatThrownBy(() -> loginModelInterceptor.preHandle(request, response, handler))
            .isInstanceOf(PresentationException.class)
            .satisfies(e -> {
                PresentationException ex = (PresentationException) e;
                assertThat(ex.getResultCode()).isEqualTo(PresentationErrorCode.UNAUTHORIZED);
            });
    }

    private HandlerMethod createHandlerMethod(String methodName) throws NoSuchMethodException {
        TestController controller = new TestController();
        Method method = controller.getClass().getMethod(methodName);
        return new HandlerMethod(controller, method);
    }

    @RestController
    static class TestController {

        @GetMapping("/model")
        @LoginModel
        public void modelMethod() {
        }

        @GetMapping("/public")
        public void methodWithoutAnnotation() {
        }
    }
}
