package net.catsnap.global.security.util;

import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;

public interface TokenAuthentication {

    CatsnapAuthenticationToken authenticate(String token);
}
