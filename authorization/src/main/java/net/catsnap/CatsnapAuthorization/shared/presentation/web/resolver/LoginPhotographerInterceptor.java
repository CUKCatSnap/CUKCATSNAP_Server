package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginPhotographer;
import org.springframework.stereotype.Component;

@Component
public class LoginPhotographerInterceptor
    extends AbstractAuthInterceptor<LoginPhotographer, CatsnapAuthority> {

    public LoginPhotographerInterceptor() {
        super(LoginPhotographer.class, List.of(
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ));
    }
}
