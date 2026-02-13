package net.catsnap.domain.auth.interceptor;

import java.util.List;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class AnyUserInterceptor extends AbstractAuthInterceptor<AnyUser, CatsnapAuthority> {

    public AnyUserInterceptor() {
        super(AnyUser.class, List.of(CatsnapAuthority.MEMBER, CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ANONYMOUS));
    }
}
