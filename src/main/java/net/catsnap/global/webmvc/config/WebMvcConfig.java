package net.catsnap.global.webmvc.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserIdArgumentResolver;
import net.catsnap.domain.auth.interceptor.AnyUserInterceptor;
import net.catsnap.domain.auth.interceptor.LoginMemberInterceptor;
import net.catsnap.domain.auth.interceptor.LoginPhotographerInterceptor;
import net.catsnap.domain.auth.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserIdArgumentResolver userIdArgumentResolver;
    private final AnyUserInterceptor anyUserInterceptor;
    private final LoginMemberInterceptor loginMemberInterceptor;
    private final LoginPhotographerInterceptor loginPhotographerInterceptor;
    private final LoginUserInterceptor loginUserInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(anyUserInterceptor);
        registry.addInterceptor(loginMemberInterceptor);
        registry.addInterceptor(loginPhotographerInterceptor);
        registry.addInterceptor(loginUserInterceptor);
    }
}
