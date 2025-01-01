package net.catsnap.support.security;

import java.util.List;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AnonymousSecurityContext {

    public static void setContext() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
            new CatsnapAuthenticationToken(
                "test",
                "test",
                List.of(CatsnapAuthority.ANONYMOUS),
                -1L
            ));
        SecurityContextHolder.setContext(securityContext);
    }

    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }

}
