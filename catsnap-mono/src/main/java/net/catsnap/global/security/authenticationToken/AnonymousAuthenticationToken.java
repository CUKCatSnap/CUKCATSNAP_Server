package net.catsnap.global.security.authenticationToken;

import java.util.Collection;
import net.catsnap.domain.user.fakeuser.entity.FakeUser;
import org.springframework.security.core.GrantedAuthority;

public class AnonymousAuthenticationToken extends CatsnapAuthenticationToken {

    public AnonymousAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AnonymousAuthenticationToken(Object principal,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, FakeUser.fakeUserIdentifier, authorities, FakeUser.fakeUserId);
    }
}
