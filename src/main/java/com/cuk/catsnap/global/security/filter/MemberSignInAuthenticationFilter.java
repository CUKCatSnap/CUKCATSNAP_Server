package com.cuk.catsnap.global.security.filter;

import com.cuk.catsnap.global.result.ResultCode;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.SecurityResultCode;
import com.cuk.catsnap.global.result.errorcode.SecurityErrorCode;
import com.cuk.catsnap.global.security.authentication.MemberAuthentication;
import com.cuk.catsnap.global.security.dto.SecurityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


@Component
public class MemberSignInAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final SecretKey key;

    protected MemberSignInAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, SecretKey key) {
        super("/member/signin/catsnap", authenticationManager);
        this.objectMapper = objectMapper;
        this.key = key;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SecurityRequest.MemberSingInRequest securityRequest = null;
        try{
            ServletInputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            securityRequest= objectMapper.readValue(body, SecurityRequest.MemberSingInRequest.class);
        } catch (IOException e) {
            /*
            * API 요청 형식이 잘못되었을 때 발생하는 예외 처리. AuthenticationException은 인증 과정에서 발생하는 문제와 관련된
            *  것이므로 이것을 상속한 예외를 사용하지 않는다.
             */
            responseBody(response, SecurityErrorCode.BAD_API_FORM);
            return null;
        }

        String identifier = securityRequest.getIdentifier();
        String password = securityRequest.getPassword();
        Authentication beforeAuthentication = new MemberAuthentication(identifier, password);
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        return authenticationManager.authenticate(beforeAuthentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {

        /*
         * accessToken을 생성하는 부분. 1시간 동안 유효(시간 * 분 * 초 * ms)
         */
        String accessToken = setJwtToken("catsnap","accessToken", authResult.getPrincipal(),
                authResult.getAuthorities(),authResult.getDetails(),1L * 60L * 60L* 1000L);

        /*
         * refreshToken을 생성하는 부분. 30일 동안 유효30일(일 * 시간 * 분 * 초 * ms)
         */
        String refreshToken = setJwtToken("catsnap","refreshToken", authResult.getPrincipal(),
                authResult.getAuthorities(),authResult.getDetails(),30L * 24L * 60L * 60L * 1000L);

        Cookie accessTokenCookie = new Cookie("refreshToken", refreshToken);
        accessTokenCookie.setMaxAge(30 * 24 * 60 *60); //쿠키 만료 시간. 단위 : s, 30일(일 * 시간 * 분 * 초)
        accessTokenCookie.setHttpOnly(true); // 클라이언트 측에서 쿠키 접근 금지
        accessTokenCookie.setSecure(true); // https에서만 쿠키 전송

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(accessTokenCookie);
        responseBody(response, SecurityResultCode.COMPLETE_SIGN_IN);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        responseBody(response, SecurityErrorCode.WRONG_ID_OR_PASSWORD);
    }

    /*
        * JWT 토큰 생성 메서드.
        *  expiration은 ms 단위임.
        * principal은 로그인 시 사용하는 id값이고  id는 데이터베이스에서 사용되는 유저의 id값임.
     */
    private String setJwtToken(Object provider, Object type, Object principal, Object authorities, Object id, Long expiration) {
        return Jwts.builder()
                .setHeader(Map.of(
                        "provider", provider,
                        "type", type
                ))
                .setClaims(Map.of(
                        "identifier", principal,
                        "authorities", authorities,
                        "id", id
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    private void responseBody(HttpServletResponse response, ResultCode resultCode) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(ResultResponse.of(resultCode));
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
