package net.catsnap.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.code.SecurityResultCode;
import net.catsnap.global.security.dto.AccessTokenResponse;
import net.catsnap.global.security.dto.AuthTokenDTO;
import net.catsnap.global.security.util.AuthTokenIssuer;
import net.catsnap.global.security.util.ServletSecurityResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ServletSecurityResponse servletSecurityResponse;
    private final AuthTokenIssuer authTokenIssuer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        AuthTokenDTO authTokenDTO = authTokenIssuer.issueAuthToken(authentication);

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
}
