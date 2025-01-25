package net.catsnap.global.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.errorcode.SecurityErrorCode;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.util.ServletSecurityResponse;
import net.catsnap.global.security.util.TokenAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ServletSecurityResponse servletSecurityResponse;
    private final TokenAuthentication tokenAuthentication;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            filterChain.doFilter(request, response);
        } else {
            jwt = parseJwt(jwt);
            try {
                CatsnapAuthenticationToken authenticationToken = tokenAuthentication.authenticate(
                    jwt);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
            /*
             * JWT 토큰 서명이 올바르지 않은 경우
             * Jwt 토큰의 서명이 없는 경우
             */ catch (SignatureException | UnsupportedJwtException | MalformedJwtException e) {
                unsuccessfulAuthentication(request, response, SecurityErrorCode.WRONG_JWT_TOKEN);
            }
            /*
             * JWT 토큰이 만료된 경우
             */ catch (ExpiredJwtException e) {
                unsuccessfulAuthentication(request, response, SecurityErrorCode.EXPIRED_JWT_TOKEN);
            }
        }
    }

    /*
     * JWT 토큰에서 "Bearer "를 제거하는 메서드
     * */
    private String parseJwt(String jwt) {
        return jwt.replace("Bearer ", "");
    }


    private void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, SecurityErrorCode securityErrorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletSecurityResponse.responseBody(response, securityErrorCode);
    }
}
