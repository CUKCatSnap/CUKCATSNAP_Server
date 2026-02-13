package net.catsnap.domain.auth.interceptor;

import java.util.List;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class LoginUserInterceptor extends AbstractAuthInterceptor<LoginUser, CatsnapAuthority> {

    public LoginUserInterceptor() {
        super(LoginUser.class, List.of(CatsnapAuthority.MEMBER, CatsnapAuthority.PHOTOGRAPHER));
    }
}
