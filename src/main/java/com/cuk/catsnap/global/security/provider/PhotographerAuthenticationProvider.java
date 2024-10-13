package com.cuk.catsnap.global.security.provider;

import com.cuk.catsnap.global.security.authentication.PhotographerAuthentication;
import com.cuk.catsnap.global.security.service.PhotographerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PhotographerAuthenticationProvider implements AuthenticationProvider {

    private final PhotographerDetailsService photographerDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails photographerDetails = photographerDetailsService.loadUserByUsername(username);

        if(passwordEncoder.matches(password,photographerDetails.getPassword())){
            Long photographerId = photographerDetailsService.getPhotographerId(username);
            return new PhotographerAuthentication(
                    photographerDetails.getUsername(),
                    photographerDetails.getPassword(),
                    photographerDetails.getAuthorities(),
                    photographerId
            );
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhotographerAuthentication.class.isAssignableFrom(authentication);
    }
}
