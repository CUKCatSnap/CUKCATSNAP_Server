package com.cuk.catsnap.global.security.contextholder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetAuthenticationInfo {

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }

}
