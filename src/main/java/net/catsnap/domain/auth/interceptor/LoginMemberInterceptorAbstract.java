package net.catsnap.domain.auth.interceptor;

import java.util.List;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberInterceptorAbstract
    extends AbstractAuthInterceptor<LoginMember, CatsnapAuthority> {

    public LoginMemberInterceptorAbstract() {
        super(LoginMember.class, List.of(CatsnapAuthority.MEMBER));
    }
}
