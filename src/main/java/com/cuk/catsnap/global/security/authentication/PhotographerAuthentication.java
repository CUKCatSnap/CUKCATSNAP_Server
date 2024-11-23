package com.cuk.catsnap.global.security.authentication;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class PhotographerAuthentication extends CatsnapAuthenticationToken {

    public PhotographerAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public PhotographerAuthentication(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities, Long photographerId) {
        super(principal, credentials, authorities, photographerId);
    }
}
