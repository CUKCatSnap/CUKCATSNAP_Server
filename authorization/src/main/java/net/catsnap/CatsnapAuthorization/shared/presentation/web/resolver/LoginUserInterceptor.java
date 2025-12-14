package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginUser;
import org.springframework.stereotype.Component;

@Component
public class LoginUserInterceptor extends AbstractAuthInterceptor<LoginUser, CatsnapAuthority> {

    public LoginUserInterceptor() {
        super(LoginUser.class, List.of(
            CatsnapAuthority.MODEL,
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ));
    }
}
