package com.cuk.catsnap.global.security;

import com.cuk.catsnap.global.security.filter.MemberSignInAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberSignInAuthenticationFilter memberSignInAuthenticationFilter;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
            .addFilterAt(memberSignInAuthenticationFilter, BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests->
                    authorizeRequests
                    .anyRequest().permitAll()
            );
        return http.build();
    }
}
