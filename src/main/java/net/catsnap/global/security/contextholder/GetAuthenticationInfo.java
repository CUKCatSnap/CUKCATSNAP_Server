package net.catsnap.global.security.contextholder;

import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetAuthenticationInfo {

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }

    public static CatsnapAuthority getAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
            .map(CatsnapAuthority.class::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));
    }
}
