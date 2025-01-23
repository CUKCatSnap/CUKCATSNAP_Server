package net.catsnap.global.security.provider;

import lombok.RequiredArgsConstructor;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.service.CatsnapUserDetailsService;
import net.catsnap.global.security.userdetail.CatsnapUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CatsnapAuthenticationProvider implements AuthenticationProvider {

    private final CatsnapUserDetailsService catsnapUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String identifier = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = catsnapUserDetailsService.loadUserByUsername(identifier);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new CatsnapAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities(),
                ((CatsnapUserDetails) userDetails).getDatabaseId());
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CatsnapAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
