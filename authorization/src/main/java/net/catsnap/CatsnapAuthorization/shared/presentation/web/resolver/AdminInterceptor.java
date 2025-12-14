package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.Admin;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.stereotype.Component;

@Component
public class AdminInterceptor extends AbstractAuthInterceptor<Admin, CatsnapAuthority> {

    public AdminInterceptor() {
        super(Admin.class, List.of(CatsnapAuthority.ADMIN));
    }
}