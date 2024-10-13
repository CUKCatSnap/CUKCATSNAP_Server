package com.cuk.catsnap.global.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhotographerAuthentication extends UsernamePasswordAuthenticationToken {

    private final Long photographerId;

    public PhotographerAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
        this.photographerId = null;
    }

    public PhotographerAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long photographerId) {
        super(principal, credentials, authorities);
        this.photographerId = photographerId;
    }

    @Override
    public Long getDetails() {
        return photographerId;
    }
}
