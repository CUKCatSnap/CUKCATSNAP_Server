package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LonginModel;
import org.springframework.stereotype.Component;

@Component
public class LoginModelInterceptor extends AbstractAuthInterceptor<LonginModel, CatsnapAuthority> {

    public LoginModelInterceptor() {
        super(LonginModel.class, List.of(
            CatsnapAuthority.MODEL,
            CatsnapAuthority.ADMIN
        ));
    }
}