package com.cuk.catsnap.global.security.filter;

import com.cuk.catsnap.global.result.ResultCode;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.SecurityResultCode;
import com.cuk.catsnap.global.result.errorcode.SecurityErrorCode;
import com.cuk.catsnap.global.security.authentication.MemberAuthentication;
import com.cuk.catsnap.global.security.dto.SecurityRequest;
import com.cuk.catsnap.global.security.util.ServletSecurityResponse;
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
    private ServletSecurityResponse servletSecurityResponse;

    protected MemberSignInAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super("/member/signin/catsnap", authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SecurityRequest.CatsnapSingInRequest catsnapSingInRequest = null;
        try{
            ServletInputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            catsnapSingInRequest= objectMapper.readValue(body, SecurityRequest.CatsnapSingInRequest.class);
        } catch (IOException e) {
            /*
            * API 요청 형식이 잘못되었을 때 발생하는 예외 처리. AuthenticationException은 인증 과정에서 발생하는 문제와 관련된
            *  것이므로 이것을 상속한 예외를 사용하지 않는다.
             */
            servletSecurityResponse.responseBody(response, SecurityErrorCode.BAD_API_FORM);
            return null;
        }

        String identifier = catsnapSingInRequest.getIdentifier();
        String password = catsnapSingInRequest.getPassword();
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
        String accessToken = servletSecurityResponse.setJwtToken("catsnap","accessToken", authResult.getPrincipal(),
                authResult.getAuthorities(),authResult.getDetails(),1L * 60L * 60L* 1000L);

        /*
         * refreshToken을 생성하는 부분. 30일 동안 유효30일(일 * 시간 * 분 * 초 * ms)
         */
        String refreshToken = servletSecurityResponse.setJwtToken("catsnap","refreshToken", authResult.getPrincipal(),
                authResult.getAuthorities(),authResult.getDetails(),30L * 24L * 60L * 60L * 1000L);

        Cookie accessTokenCookie = new Cookie("refreshToken", refreshToken);
        accessTokenCookie.setMaxAge(30 * 24 * 60 *60); //쿠키 만료 시간. 단위 : s, 30일(일 * 시간 * 분 * 초)
        accessTokenCookie.setHttpOnly(true); // 클라이언트 측에서 쿠키 접근 금지
        accessTokenCookie.setSecure(true); // https에서만 쿠키 전송

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(accessTokenCookie);
        servletSecurityResponse.responseBody(response, SecurityResultCode.COMPLETE_SIGN_IN);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletSecurityResponse.responseBody(response, SecurityErrorCode.WRONG_ID_OR_PASSWORD);
    }

}
