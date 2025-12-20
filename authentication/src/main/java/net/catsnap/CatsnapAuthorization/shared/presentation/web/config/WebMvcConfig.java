package net.catsnap.CatsnapAuthorization.shared.presentation.web.config;

import net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver.AdminInterceptor;
import net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver.AnyUserInterceptor;
import net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver.LoginModelInterceptor;
import net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver.LoginPhotographerInterceptor;
import net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;
    private final AnyUserInterceptor anyUserInterceptor;
    private final LoginUserInterceptor loginUserInterceptor;
    private final LoginPhotographerInterceptor loginPhotographerInterceptor;
    private final LoginModelInterceptor loginModelInterceptor;

    public WebMvcConfig(
        AdminInterceptor adminInterceptor,
        AnyUserInterceptor anyUserInterceptor,
        LoginUserInterceptor loginUserInterceptor,
        LoginPhotographerInterceptor loginPhotographerInterceptor,
        LoginModelInterceptor loginModelInterceptor
    ) {
        this.adminInterceptor = adminInterceptor;
        this.anyUserInterceptor = anyUserInterceptor;
        this.loginUserInterceptor = loginUserInterceptor;
        this.loginPhotographerInterceptor = loginPhotographerInterceptor;
        this.loginModelInterceptor = loginModelInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor);
        registry.addInterceptor(anyUserInterceptor);
        registry.addInterceptor(loginUserInterceptor);
        registry.addInterceptor(loginPhotographerInterceptor);
        registry.addInterceptor(loginModelInterceptor);
    }
}
