package net.catsnap.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.filter.JwtAuthenticationFilter;
import net.catsnap.global.security.filter.MemberSignInAuthenticationFilter;
import net.catsnap.global.security.filter.PhotographerSignInAuthenticationFilter;
import net.catsnap.global.security.filter.RefreshAccessTokenFilter;
import net.catsnap.global.security.provider.MemberAuthenticationProvider;
import net.catsnap.global.security.provider.PhotographerAuthenticationProvider;
import net.catsnap.global.security.service.MemberDetailsService;
import net.catsnap.global.security.service.PhotographerDetailsService;
import net.catsnap.global.security.util.JwtTokenAuthentication;
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
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;
    private final SecretKey secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberDetailsService memberDetailsService() {
        return new MemberDetailsService(memberRepository);
    }

    @Bean
    public PhotographerDetailsService photographerDetailsService() {
        return new PhotographerDetailsService(photographerRepository);
    }

    @Bean
    public MemberAuthenticationProvider memberAuthenticationProvider() {
        return new MemberAuthenticationProvider(memberDetailsService(), passwordEncoder());
    }

    @Bean
    public PhotographerAuthenticationProvider photographerAuthenticationProvider() {
        return new PhotographerAuthenticationProvider(photographerDetailsService(),
            passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(memberAuthenticationProvider());
        providers.add(photographerAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public JwtTokenAuthentication jwtTokenAuthentication() {
        return new JwtTokenAuthentication(secretKey);
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


    /*
     * 로그인, 회원가입 관련 필터 설정
     */
    @Bean
    public SecurityFilterChain signInUpConfig(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/member/signup/catsnap", "/photographer/signup/catsnap",
                "/member/signin/catsnap", "/photographer/signin/catsnap", "/refresh/access-token")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .addFilterAt(
                new MemberSignInAuthenticationFilter(authenticationManager(), objectMapper,
                    servletSecurityResponse),
                BasicAuthenticationFilter.class
            )
            .addFilterAt(
                new PhotographerSignInAuthenticationFilter(authenticationManager(), objectMapper,
                    servletSecurityResponse),
                BasicAuthenticationFilter.class
            )
            .addFilterAt(
                new RefreshAccessTokenFilter(servletSecurityResponse, jwtTokenAuthentication()),
                BasicAuthenticationFilter.class
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().permitAll()
            );
        return http.build();
    }

    /*
     * Photographer 계정으로 로그인 후 접근해야 하는 리소스에 대한 필터 설정
     */
    @Bean
    public SecurityFilterChain authenticatedPhotographerConfig(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/reservation/photographer/my/**", "/photographer/my/**")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .addFilterAt(
                new JwtAuthenticationFilter(servletSecurityResponse, jwtTokenAuthentication()),
                BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().hasAuthority(CatsnapAuthority.PHOTOGRAPHER.name())
            );
        return http.build();
    }

    /*
     * Member 계정으로 로그인 후 접근해야 하는 리소스에 대한 필터 설정
     */
    @Bean
    public SecurityFilterChain authenticatedMemberConfig(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/reservation/member/my/**", "/reservation/member/book", "/review")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .addFilterAt(
                new JwtAuthenticationFilter(servletSecurityResponse, jwtTokenAuthentication()),
                BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().hasAuthority(CatsnapAuthority.MEMBER.name())
            );
        return http.build();
    }

    /*
     * Photographer 또는 Member 계정으로 로그인 후 접근해야 하는 리소스에 대한 필터 설정
     */
    @Bean
    public SecurityFilterChain authenticatedUserConfig(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/review/like/*")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .addFilterAt(
                new JwtAuthenticationFilter(servletSecurityResponse, jwtTokenAuthentication()),
                BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().hasAnyAuthority(CatsnapAuthority.MEMBER.name(),
                        CatsnapAuthority.PHOTOGRAPHER.name())
            );
        return http.build();
    }

    /*
     * 로그인 없이 접근 가능한 리소스에 대한 필터 설정
     */
    @Bean
    public SecurityFilterChain publicResourceConfig(HttpSecurity http) throws Exception {
        http.securityMatcher("/**")
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest().permitAll()
            )
            .anonymous(
                anonymousConfigurer -> anonymousConfigurer
                    .principal("anonymous")
                    .authorities(List.of(CatsnapAuthority.ANONYMOUS))
            );
        return http.build();
    }
}
