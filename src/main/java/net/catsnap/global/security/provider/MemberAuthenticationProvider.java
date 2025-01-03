package net.catsnap.global.security.provider;

import net.catsnap.global.security.authenticationToken.MemberAuthenticationToken;
import net.catsnap.global.security.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String identifier = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails memberDetails = memberDetailsService.loadUserByUsername(identifier);

        if (passwordEncoder.matches(password, memberDetails.getPassword())) {
            Long memberId = memberDetailsService.getMemberId(identifier);
            return new MemberAuthenticationToken(
                memberDetails.getUsername(),
                memberDetails.getPassword(),
                memberDetails.getAuthorities(),
                memberId);
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MemberAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
