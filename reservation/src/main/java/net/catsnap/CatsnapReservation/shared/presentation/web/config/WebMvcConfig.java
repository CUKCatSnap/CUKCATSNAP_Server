package net.catsnap.CatsnapReservation.shared.presentation.web.config;

import java.util.List;
import net.catsnap.CatsnapReservation.shared.presentation.web.interceptor.AdminInterceptor;
import net.catsnap.CatsnapReservation.shared.presentation.web.interceptor.AnyUserInterceptor;
import net.catsnap.CatsnapReservation.shared.presentation.web.interceptor.LoginModelInterceptor;
import net.catsnap.CatsnapReservation.shared.presentation.web.interceptor.LoginPhotographerInterceptor;
import net.catsnap.CatsnapReservation.shared.presentation.web.interceptor.LoginUserInterceptor;
import net.catsnap.CatsnapReservation.shared.presentation.web.resolver.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 설정 클래스.
 * <p>
 * 인터셉터와 Argument Resolver를 등록하여 웹 요청 처리를 구성합니다.
 * </p>
 *
 * @see WebMvcConfigurer
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;
    private final AnyUserInterceptor anyUserInterceptor;
    private final LoginUserInterceptor loginUserInterceptor;
    private final LoginPhotographerInterceptor loginPhotographerInterceptor;
    private final LoginModelInterceptor loginModelInterceptor;
    private final UserIdArgumentResolver userIdArgumentResolver;

    /**
     * WebMvcConfig 생성자.
     *
     * @param adminInterceptor           관리자 권한 검증 인터셉터
     * @param anyUserInterceptor         모든 사용자 권한 검증 인터셉터
     * @param loginUserInterceptor       일반 사용자 로그인 검증 인터셉터
     * @param loginPhotographerInterceptor 작가 로그인 검증 인터셉터
     * @param loginModelInterceptor      모델 로그인 검증 인터셉터
     * @param userIdArgumentResolver     사용자 ID Argument Resolver
     */
    public WebMvcConfig(
        AdminInterceptor adminInterceptor,
        AnyUserInterceptor anyUserInterceptor,
        LoginUserInterceptor loginUserInterceptor,
        LoginPhotographerInterceptor loginPhotographerInterceptor,
        LoginModelInterceptor loginModelInterceptor,
        UserIdArgumentResolver userIdArgumentResolver
    ) {
        this.adminInterceptor = adminInterceptor;
        this.anyUserInterceptor = anyUserInterceptor;
        this.loginUserInterceptor = loginUserInterceptor;
        this.loginPhotographerInterceptor = loginPhotographerInterceptor;
        this.loginModelInterceptor = loginModelInterceptor;
        this.userIdArgumentResolver = userIdArgumentResolver;
    }

    /**
     * 인터셉터를 등록합니다.
     * <p>
     * 등록 순서대로 인터셉터가 실행됩니다.
     * </p>
     *
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor);
        registry.addInterceptor(anyUserInterceptor);
        registry.addInterceptor(loginUserInterceptor);
        registry.addInterceptor(loginPhotographerInterceptor);
        registry.addInterceptor(loginModelInterceptor);
    }

    /**
     * Argument Resolver를 등록합니다.
     *
     * @param resolvers Argument Resolver 목록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }
}
