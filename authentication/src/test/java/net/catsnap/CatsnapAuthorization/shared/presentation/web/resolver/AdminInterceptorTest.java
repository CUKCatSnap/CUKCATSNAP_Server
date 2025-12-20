package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthenticationException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthorizationException;
import net.catsnap.shared.auth.Admin;
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
class AdminInterceptorTest {

    private AdminInterceptor adminInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        adminInterceptor = new AdminInterceptor();
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
        request.addHeader("X-Authority", "admin");
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertTrue(adminInterceptor.preHandle(request, response, handler));
    }

    @Test
    void 인증_헤더가_없으면_AuthenticationException_발생합니다() throws Exception {
        // given
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertThrows(AuthenticationException.class,
            () -> adminInterceptor.preHandle(request, response, handler));
    }

    @Test
    void ADMIN이_아닌_권한이면_AuthorizationException_발생합니다() throws Exception {
        // given
        request.addHeader("X-Authority", "photographer");
        HandlerMethod handler = createHandlerMethod("adminMethod");

        // when & then
        assertThrows(AuthorizationException.class,
            () -> adminInterceptor.preHandle(request, response, handler));
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