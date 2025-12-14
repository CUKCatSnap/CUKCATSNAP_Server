package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthenticationException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.AuthorizationException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.SecurityErrorCode;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 인증/인가를 처리하는 인터셉터의 추상 클래스입니다.
 * <p>
 * 특정 어노테이션이 붙은 컨트롤러 메서드에 대해 권한을 검증합니다. 하위 클래스는 검증할 어노테이션 타입과 허용할 권한 목록을 지정하여 구체적인 인터셉터를 구현합니다.
 * </p>
 *
 * @param <A> 검증 대상 어노테이션 타입
 */
public abstract class AbstractAuthInterceptor<A extends Annotation> implements
    HandlerInterceptor {

    /**
     * Passport 헤더: 사용자 권한
     */
    private static final String AUTHORITY_HEADER = "X-Authority";

    /**
     * 검증할 어노테이션의 타입
     */
    private final Class<A> targetAnnotationType;

    /**
     * 접근을 허용할 권한 목록
     */
    private final List<CatsnapAuthority> allowedAuthorities;

    /**
     * 인터셉터를 생성합니다.
     *
     * @param targetAnnotationType 검증할 어노테이션 클래스
     * @param allowedAuthorities   접근을 허용할 권한 목록
     */
    protected AbstractAuthInterceptor(Class<A> targetAnnotationType, List<CatsnapAuthority> allowedAuthorities) {
        this.targetAnnotationType = targetAnnotationType;
        this.allowedAuthorities = allowedAuthorities;
    }

    /**
     * 요청 전처리 핸들러입니다.
     * <p>
     * 다음 순서로 검증을 수행합니다:
     * <ol>
     *   <li>핸들러가 컨트롤러 메서드인지 확인</li>
     *   <li>대상 어노테이션이 메서드에 존재하는지 확인</li>
     *   <li>사용자의 권한이 허용 목록에 포함되는지 검증</li>
     * </ol>
     * </p>
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param handler  핸들러 객체
     * @return 다음 인터셉터 체인 진행 여부 (true: 진행, false: 중단)
     * @throws Exception 예외 발생 시
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        // 1. 핸들러가 컨트롤러 메서드인지 확인
        HandlerMethod handlerMethod = extractHandlerMethod(handler);
        if (handlerMethod == null) {
            return true;
        }

        // 2. 대상 어노테이션이 메서드에 존재하는지 확인
        A annotation = findAnnotationOnMethod(handlerMethod);
        if (annotation == null) {
            return true;
        }

        // 3. 사용자 권한 검증
        validateUserAuthority(request);
        return true;
    }

    /**
     * 핸들러 객체가 HandlerMethod인 경우 추출합니다.
     *
     * @param handler 핸들러 객체
     * @return HandlerMethod 또는 null (HandlerMethod가 아닌 경우)
     */
    private HandlerMethod extractHandlerMethod(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return null;
        }
        return handlerMethod;
    }

    /**
     * 메서드에 대상 어노테이션이 존재하는지 확인합니다.
     *
     * @param handlerMethod 핸들러 메서드
     * @return 어노테이션 객체 또는 null (어노테이션이 없는 경우)
     */
    private A findAnnotationOnMethod(HandlerMethod handlerMethod) {
        return handlerMethod.getMethodAnnotation(targetAnnotationType);
    }

    /**
     * 사용자의 권한을 검증합니다.
     * <p>
     * 현재 사용자의 권한이 허용된 권한 목록({@link #allowedAuthorities})에 포함되는지 확인합니다.
     * 게이트웨이에서 발급한 Passport 헤더(X-Authority)를 통해 사용자 권한을 확인합니다.
     * </p>
     *
     * @param request HTTP 요청 객체
     * @throws AuthenticationException 인증 정보가 없거나 유효하지 않은 경우 (401)
     * @throws AuthorizationException 접근 권한이 없는 경우 (403)
     */
    protected void validateUserAuthority(HttpServletRequest request) {
        String authorityHeader = request.getHeader(AUTHORITY_HEADER);

        // 1. 인증 헤더가 없는 경우 예외 발생
        if (authorityHeader == null || authorityHeader.isBlank()) {
            throw new AuthenticationException(SecurityErrorCode.UNAUTHORIZED);
        }

        // 2. 헤더 값을 CatsnapAuthority enum으로 변환
        CatsnapAuthority userAuthority = CatsnapAuthority.findAuthorityByName(authorityHeader)
            .orElseThrow(() -> new AuthenticationException(SecurityErrorCode.INVALID_AUTHORITY));

        // 3. 사용자 권한이 허용 목록에 포함되는지 확인
        if (!allowedAuthorities.contains(userAuthority)) {
            throw new AuthorizationException(SecurityErrorCode.FORBIDDEN);
        }
    }
}
