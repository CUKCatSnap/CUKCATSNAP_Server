package com.cuk.catsnap.support.security;

import com.cuk.catsnap.global.security.authentication.MemberAuthenticationToken;
import com.cuk.catsnap.global.security.authority.CatsnapAuthority;
import java.util.List;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberSecurityContext {

    public static final Long MEMBER_ID = 9876543210L;

    public static void setContext() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
            new MemberAuthenticationToken(
                "test",
                "test",
                List.of(CatsnapAuthority.MEMBER),
                MEMBER_ID
            ));
        SecurityContextHolder.setContext(securityContext);
    }

    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
