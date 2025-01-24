package net.catsnap.global.webmvc.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.AnyUserArgumentResolver;
import net.catsnap.domain.auth.argumentresolver.LoginMemberArgumentResolver;
import net.catsnap.domain.auth.argumentresolver.LoginPhotographerArgumentResolver;
import net.catsnap.domain.auth.argumentresolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final LoginPhotographerArgumentResolver loginPhotographerArgumentResolver;
    private final AnyUserArgumentResolver anyUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(loginPhotographerArgumentResolver);
        resolvers.add(anyUserArgumentResolver);
    }
}
