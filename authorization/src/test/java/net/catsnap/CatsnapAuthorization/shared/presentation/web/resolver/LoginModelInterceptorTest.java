package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthenticationException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthorizationException;
import net.catsnap.shared.auth.LonginModel;
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
class LoginModelInterceptorTest {

    private LoginModelInterceptor loginModelInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        loginModelInterceptor = new LoginModelInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void LonginModel_어노테이션을_붙이지_않으면_통과한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("methodWithoutAnnotation");

        // when & then
        assertTrue(loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void MODEL_권한이_있으면_통과한다() throws Exception {
        // given
        request.addHeader("X-Authority", "model");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertDoesNotThrow(() -> loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void ADMIN_권한이_있으면_통과한다() throws Exception {
        // given
        request.addHeader("X-Authority", "admin");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertDoesNotThrow(() -> loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void PHOTOGRAPHER_권한이면_AuthorizationException_발생한다() throws Exception {
        // given
        request.addHeader("X-Authority", "photographer");
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertThrows(AuthorizationException.class,
            () -> loginModelInterceptor.preHandle(request, response, handler));
    }

    @Test
    void 인증_헤더가_없으면_AuthenticationException_발생한다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("modelMethod");

        // when & then
        assertThrows(AuthenticationException.class,
            () -> loginModelInterceptor.preHandle(request, response, handler));
    }

    private HandlerMethod createHandlerMethod(String methodName) throws NoSuchMethodException {
        return new HandlerMethod(new TestController(), methodName);
    }

    @RestController
    static class TestController {

        @GetMapping("/model")
        @LonginModel
        public void modelMethod() {
        }

        @GetMapping("/public")
        public void methodWithoutAnnotation() {
        }
    }
}