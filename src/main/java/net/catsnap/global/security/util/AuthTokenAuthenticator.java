package net.catsnap.global.security.util;

import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;

public interface AuthTokenAuthenticator {

    CatsnapAuthenticationToken authTokenAuthenticate(String token);
}
