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
import net.catsnap.shared.auth.Admin;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import net.catsnap.shared.passport.domain.exception.InvalidPassportException;
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

@DisplayName("AdminInterceptor 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminInterceptorTest {

    private AdminInterceptor adminInterceptor;
    private PassportHandler passportHandler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        passportHandler = mock(PassportHandler.class);
        adminInterceptor = new AdminInterceptor(passportHandler);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void Admin_어노테이션을_붙이지_않으면_통과한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("methodWithoutAnnotation");

        // when & then
        assertTrue(adminInterceptor.preHandle(request, response, handler));
    }

    @Test
    void ADMIN_권한이_있으면_통과한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport adminPassport = new Passport((byte) 1, 1L, CatsnapAuthority.ADMIN, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(adminPassport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertTrue(adminInterceptor.preHandle(request, response, handler));
    }

    @Test
    void 인증_헤더가_없으면_UNAUTHORIZED_예외가_발생한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertThatThrownBy(() -> adminInterceptor.preHandle(request, response, handler))
            .isInstanceOf(PresentationException.class)
            .satisfies(e -> {
                PresentationException ex = (PresentationException) e;
                assertThat(ex.getResultCode()).isEqualTo(PresentationErrorCode.UNAUTHORIZED);
            });
    }

    @Test
    void ADMIN이_아닌_권한이면_FORBIDDEN_예외가_발생한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport photographerPassport = new Passport((byte) 1, 2L, CatsnapAuthority.PHOTOGRAPHER,
            now, exp);

        when(passportHandler.parse(anyString())).thenReturn(photographerPassport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertThatThrownBy(() -> adminInterceptor.preHandle(request, response, handler))
            .isInstanceOf(PresentationException.class)
            .satisfies(e -> {
                PresentationException ex = (PresentationException) e;
                assertThat(ex.getResultCode()).isEqualTo(PresentationErrorCode.FORBIDDEN);
            });
    }

    @Test
    void 유효하지_않은_Passport면_INVALID_PASSPORT_예외가_발생한다() throws Exception {
        // given
        when(passportHandler.parse(anyString())).thenThrow(
            new InvalidPassportException("Invalid passport"));

        request.addHeader("X-Passport", "invalid-passport");
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertThatThrownBy(() -> adminInterceptor.preHandle(request, response, handler))
            .isInstanceOf(PresentationException.class)
            .satisfies(e -> {
                PresentationException ex = (PresentationException) e;
                assertThat(ex.getResultCode()).isEqualTo(PresentationErrorCode.INVALID_PASSPORT);
            });
    }

    private HandlerMethod createHandlerMethod(String methodName) throws NoSuchMethodException {
        TestController controller = new TestController();
        Method method = controller.getClass().getMethod(methodName);
        return new HandlerMethod(controller, method);
    }

    @RestController
    static class TestController {

        @GetMapping("/admin")
        @Admin
        public void adminMethod() {
        }

        @GetMapping("/public")
        public void methodWithoutAnnotation() {
        }
    }
}
