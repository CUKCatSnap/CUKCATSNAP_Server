package net.catsnap.global.security.authenticationToken;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CatsnapAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long id;

    public CatsnapAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        id = null;
    }

    public CatsnapAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities, Long id) {
        super(principal, credentials, authorities);
        this.id = id;
    }

    /*
     * 인증 완료 후 데이터베이스에서 사용되는 유저의 Id를 반환.
     */
    @Override
    public Long getDetails() {
        return id;
    }
}
