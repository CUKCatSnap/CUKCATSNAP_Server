package com.cuk.catsnap.global.security.authentication;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class MemberAuthentication extends CatsnapAuthenticationToken {

    public MemberAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MemberAuthentication(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities, Long memberId) {
        super(principal, credentials, authorities, memberId);
    }
}
