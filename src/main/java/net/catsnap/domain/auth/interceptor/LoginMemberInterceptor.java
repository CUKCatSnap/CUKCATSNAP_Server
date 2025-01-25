package net.catsnap.domain.auth.interceptor;

import java.util.List;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberInterceptor extends AbstractAuthInterceptor<LoginMember, CatsnapAuthority> {

    public LoginMemberInterceptor() {
        super(LoginMember.class, List.of(CatsnapAuthority.MEMBER));
    }
}
