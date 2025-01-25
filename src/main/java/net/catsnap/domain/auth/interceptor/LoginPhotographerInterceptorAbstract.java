package net.catsnap.domain.auth.interceptor;

import java.util.List;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class LoginPhotographerInterceptorAbstract
    extends AbstractAuthInterceptor<LoginPhotographer, CatsnapAuthority> {

    public LoginPhotographerInterceptorAbstract() {
        super(LoginPhotographer.class, List.of(CatsnapAuthority.PHOTOGRAPHER));
    }
}
