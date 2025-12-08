package net.catsnap.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.catsnap.global.result.code.SecurityResultCode;
import net.catsnap.global.result.errorcode.SecurityErrorCode;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.dto.AccessTokenResponse;
import net.catsnap.global.security.dto.AuthTokenDTO;
import net.catsnap.global.security.dto.SecurityRequest;
import net.catsnap.global.security.util.AuthTokenIssuer;
import net.catsnap.global.security.util.ServletSecurityResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

public class SignInAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final ServletSecurityResponse servletSecurityResponse;
    private final AuthTokenIssuer authTokenIssuer;

    public SignInAuthenticationFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper, ServletSecurityResponse servletSecurityResponse,
        AuthTokenIssuer authTokenIssuer) {
        super(new AntPathRequestMatcher("/*/signin/catsnap"), authenticationManager);
        this.objectMapper = objectMapper;
        this.servletSecurityResponse = servletSecurityResponse;
        this.authTokenIssuer = authTokenIssuer;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        SecurityRequest.CatsnapSignInRequest catsnapSignInRequest = null;
        try {
            ServletInputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            catsnapSignInRequest = objectMapper.readValue(body,
                SecurityRequest.CatsnapSignInRequest.class);
        } catch (IOException e) {
            /*
             * API 요청 형식이 잘못되었을 때 발생하는 예외 처리. AuthenticationException은 인증 과정에서 발생하는 문제와 관련된
             *  것이므로 이것을 상속한 예외를 사용하지 않는다.
             */
            servletSecurityResponse.responseBody(response, SecurityErrorCode.BAD_API_FORM);
            return null;
        }

        String identifier = catsnapSignInRequest.getIdentifier();
        String password = catsnapSignInRequest.getPassword();
        CatsnapAuthenticationToken beforeAuthenticationToken = new CatsnapAuthenticationToken(
            identifier, password);
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        return authenticationManager.authenticate(beforeAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException {

        AuthTokenDTO authTokenDTO = authTokenIssuer.issueAuthToken(authResult);

        Cookie accessTokenCookie = new Cookie("refreshToken", authTokenDTO.refreshToken());
        accessTokenCookie.setMaxAge(30 * 24 * 60 * 60); //쿠키 만료 시간. 단위 : s, 30일(일 * 시간 * 분 * 초)
        accessTokenCookie.setHttpOnly(true); // 클라이언트 측에서 쿠키 접근 금지
        accessTokenCookie.setSecure(true); // https에서만 쿠키 전송
        accessTokenCookie.setPath("/refresh/access-token");

        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(accessTokenCookie);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(
            authTokenDTO.accessToken());
        servletSecurityResponse.responseBody(response, SecurityResultCode.COMPLETE_SIGN_IN,
            accessTokenResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletSecurityResponse.responseBody(response, SecurityErrorCode.WRONG_ID_OR_PASSWORD);
    }

}
