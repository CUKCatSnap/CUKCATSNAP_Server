package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.AnyUser;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class AnyUserInterceptor extends AbstractAuthInterceptor<AnyUser, CatsnapAuthority> {

    public AnyUserInterceptor() {
        super(AnyUser.class, List.of(
            CatsnapAuthority.ANONYMOUS,
            CatsnapAuthority.MODEL,
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ));
    }
}
