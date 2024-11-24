package net.catsnap.global.security.authority;

import org.springframework.security.core.GrantedAuthority;

public enum CatsnapAuthority implements GrantedAuthority {
    MEMBER, PHOTOGRAPHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
