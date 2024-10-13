package com.cuk.catsnap.global.security;

import com.cuk.catsnap.global.security.provider.MemberAuthenticationProvider;
import com.cuk.catsnap.global.security.provider.PhotographerAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /*private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final PhotographerAuthenticationProvider photographerAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        ProviderManager providerManager = (ProviderManager) authenticationManager;
        List<AuthenticationProvider> providers = new ArrayList<>(providerManager.getProviders());
        providers.add(memberAuthenticationProvider);
        providers.add(photographerAuthenticationProvider);

        return new ProviderManager(providers);
    }*/
}

/*
@Component
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
 */