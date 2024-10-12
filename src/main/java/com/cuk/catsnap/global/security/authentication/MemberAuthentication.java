package com.cuk.catsnap.global.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {

    private final Long memberId;

    public MemberAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
        memberId = null;
    }

    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long memberId) {
        super(principal, credentials, authorities);
        this.memberId = memberId;
    }

    /*
    * 인증 완료 후 데이터베이스에서 사용되는 유저의 Id를 반환.
     */
    @Override
    public Long getDetails() {
        return memberId;
    }
}
