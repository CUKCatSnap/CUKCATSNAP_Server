package net.catsnap.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.security.filter.JwtAuthenticationFilter;
import net.catsnap.global.security.filter.RefreshAccessTokenFilter;
import net.catsnap.global.security.filter.SignInAuthenticationFilter;
import net.catsnap.global.security.handler.OAuth2LoginSuccessHandler;
import net.catsnap.global.security.provider.CatsnapAuthenticationProvider;
import net.catsnap.global.security.service.CatsnapUserDetailsService;
import net.catsnap.global.security.service.MemberOAuth2UserService;
import net.catsnap.global.security.util.AuthTokenIssuer;
import net.catsnap.global.security.util.JwtAuthTokenAuthenticator;
import net.catsnap.global.security.util.ServletSecurityResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final ServletSecurityResponse servletSecurityResponse;
    private final SecretKey secretKey;
    private final MemberOAuth2UserService memberOAuth2UserService;
    private final UserRepository userRepository;
    private final AuthTokenIssuer authTokenIssuer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CatsnapUserDetailsService catsnapUserDetailsService() {
        return new CatsnapUserDetailsService(userRepository);
    }

    @Bean
    public CatsnapAuthenticationProvider CatsnapAuthenticationProvider() {
        return new CatsnapAuthenticationProvider(catsnapUserDetailsService(), passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(CatsnapAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public JwtAuthTokenAuthenticator jwtTokenAuthentication() {
        return new JwtAuthTokenAuthenticator(secretKey);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            Arrays.asList("https://api.catsnap.net", "http://localhost:8081"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(servletSecurityResponse, authTokenIssuer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .addFilterAt(
                new SignInAuthenticationFilter(authenticationManager(), objectMapper,
                    servletSecurityResponse, authTokenIssuer),
                BasicAuthenticationFilter.class
            )
            .addFilterAt(
                new RefreshAccessTokenFilter(servletSecurityResponse, jwtTokenAuthentication(),
                    authTokenIssuer),
                BasicAuthenticationFilter.class
            )
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(memberOAuth2UserService)
                )
                .successHandler(oAuth2LoginSuccessHandler())
            )
            .addFilterAt(
                new JwtAuthenticationFilter(servletSecurityResponse, jwtTokenAuthentication()),
                BasicAuthenticationFilter.class)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().permitAll()
            );
        return http.build();
    }
}
