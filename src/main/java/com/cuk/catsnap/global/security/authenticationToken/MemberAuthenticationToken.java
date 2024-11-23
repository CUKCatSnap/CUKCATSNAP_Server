package com.cuk.catsnap.global.security.authenticationToken;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class MemberAuthenticationToken extends CatsnapAuthenticationToken {

    public MemberAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MemberAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities, Long memberId) {
        super(principal, credentials, authorities, memberId);
    }
}
