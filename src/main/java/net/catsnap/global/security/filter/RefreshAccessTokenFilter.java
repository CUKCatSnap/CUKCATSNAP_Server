package net.catsnap.global.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.code.SecurityResultCode;
import net.catsnap.global.result.errorcode.SecurityErrorCode;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.dto.AccessTokenResponse;
import net.catsnap.global.security.util.ServletSecurityResponse;
import net.catsnap.global.security.util.TokenAuthentication;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class RefreshAccessTokenFilter extends OncePerRequestFilter {

    private final ServletSecurityResponse servletSecurityResponse;
    private final TokenAuthentication tokenAuthentication;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("refreshToken"))
            .findFirst();
        if (refreshTokenCookie.isEmpty()) {
            unsuccessfulAuthentication(request, response, SecurityErrorCode.NOT_AUTHENTICATED);
        } else {
            String refreshToken = refreshTokenCookie.get().getValue();
            try {
                CatsnapAuthenticationToken authenticationToken = tokenAuthentication.authenticate(
                    refreshToken);
                /*
                 * accessToken을 생성하는 부분. 1시간 동안 유효(시간 * 분 * 초 * ms)
                 */
                String accessToken = servletSecurityResponse.setJwtToken("catsnap",
                    "accessToken",
                    authenticationToken.getPrincipal(),
                    authenticationToken.getAuthorities(), authenticationToken.getDetails(),
                    1L * 60L * 60L * 1000L
                );

                /*
                 * refreshToken을 생성하는 부분. 30일 동안 유효30일(일 * 시간 * 분 * 초 * ms)
                 */
                String newRefreshToken = servletSecurityResponse.setJwtToken("catsnap",
                    "refreshToken",
                    authenticationToken.getPrincipal(),
                    authenticationToken.getAuthorities(),
                    authenticationToken.getDetails(),
                    30L * 24L * 60L * 60L * 1000L
                );

                Cookie accessTokenCookie = new Cookie("refreshToken", newRefreshToken);
                accessTokenCookie.setMaxAge(
                    30 * 24 * 60 * 60); //쿠키 만료 시간. 단위 : s, 30일(일 * 시간 * 분 * 초)
                accessTokenCookie.setHttpOnly(true); // 클라이언트 측에서 쿠키 접근 금지
                accessTokenCookie.setSecure(true); // https에서만 쿠키 전송
                accessTokenCookie.setPath("/refresh/access-token");

                response.setStatus(HttpServletResponse.SC_OK);
                response.addCookie(accessTokenCookie);
                AccessTokenResponse accessTokenResponse = AccessTokenResponse.of(accessToken);
                servletSecurityResponse.responseBody(response,
                    SecurityResultCode.COMPLETE_REFRESH_TOKEN, accessTokenResponse);
            } catch (UnsupportedJwtException | MalformedJwtException e) {
                unsuccessfulAuthentication(request, response, SecurityErrorCode.WRONG_JWT_TOKEN);
            } catch (ExpiredJwtException e) {
                unsuccessfulAuthentication(request, response, SecurityErrorCode.EXPIRED_JWT_TOKEN);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/refresh/access-token");
    }

    private void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, SecurityErrorCode securityErrorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletSecurityResponse.responseBody(response, securityErrorCode);
    }
}
