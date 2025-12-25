package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthenticationException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthorizationException;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginPhotographer;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LoginPhotographerInterceptorTest {

    private LoginPhotographerInterceptor loginPhotographerInterceptor;
    private PassportHandler passportHandler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        passportHandler = mock(PassportHandler.class);
        loginPhotographerInterceptor = new LoginPhotographerInterceptor(passportHandler);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void LoginPhotographer_어노테이션을_붙이지_않으면_통과한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("methodWithoutAnnotation");

        // when & then
        assertTrue(loginPhotographerInterceptor.preHandle(request, response, handler));
    }

    @Test
    void PHOTOGRAPHER_권한이_있으면_통과한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 1L, CatsnapAuthority.PHOTOGRAPHER, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("photographerMethod");

        // when & then
        assertTrue(loginPhotographerInterceptor.preHandle(request, response, handler));
    }

    @Test
    void ADMIN_권한이_있으면_통과한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 2L, CatsnapAuthority.ADMIN, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("photographerMethod");

        // when & then
        assertTrue(loginPhotographerInterceptor.preHandle(request, response, handler));
    }

    @Test
    void MODEL_권한이면_AuthorizationException_발생한다() throws Exception {
        // given
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant exp = now.plus(30, ChronoUnit.MINUTES);
        Passport passport = new Passport((byte) 1, 3L, CatsnapAuthority.MODEL, now, exp);

        when(passportHandler.parse(anyString())).thenReturn(passport);

        request.addHeader("X-Passport", "signed-passport-string");
        HandlerMethod handler = createHandlerMethod("photographerMethod");

        // when & then
        assertThrows(AuthorizationException.class,
            () -> loginPhotographerInterceptor.preHandle(request, response, handler));
    }

    @Test
    void 인증_헤더가_없으면_AuthenticationException_발생한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("photographerMethod");

        // when & then
        assertThrows(AuthenticationException.class,
            () -> loginPhotographerInterceptor.preHandle(request, response, handler));
    }

    private HandlerMethod createHandlerMethod(String methodName) throws NoSuchMethodException {
        TestController controller = new TestController();
        Method method = controller.getClass().getMethod(methodName);
        return new HandlerMethod(controller, method);
    }

    @RestController
    static class TestController {

        @GetMapping("/photographer")
        @LoginPhotographer
        public void photographerMethod() {
        }

        @GetMapping("/public")
        public void methodWithoutAnnotation() {
        }
    }
}
