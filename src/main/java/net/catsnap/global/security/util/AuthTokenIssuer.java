package net.catsnap.global.security.util;

import net.catsnap.global.security.dto.AuthTokenDTO;
import org.springframework.security.core.Authentication;

public interface AuthTokenIssuer {

    AuthTokenDTO issueAuthToken(Authentication authentication);
}
