package com.cuk.catsnap.global.security.util;

import com.cuk.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;

public interface TokenAuthentication {

    CatsnapAuthenticationToken authenticate(String token);
}
