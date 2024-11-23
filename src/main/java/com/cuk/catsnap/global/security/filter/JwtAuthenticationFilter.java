package com.cuk.catsnap.global.security.filter;

import com.cuk.catsnap.global.result.errorcode.SecurityErrorCode;
import com.cuk.catsnap.global.security.authentication.MemberAuthenticationToken;
import com.cuk.catsnap.global.security.authority.CatsnapAuthority;
import com.cuk.catsnap.global.security.util.ServletSecurityResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ServletSecurityResponse servletSecurityResponse;
    private final SecretKey secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            unsuccessfulAuthentication(request, response, SecurityErrorCode.NOT_AUTHENTICATED);
        } else {
            jwt = parseJwt(jwt);
            try {
                JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
                Claims claims = jwtParser.parseClaimsJws(jwt)
                    .getBody();

                String identifier = claims.get("identifier", String.class); // 로그인 시 사용하는 id값
                Long id = claims.get("id", Long.class); // 데이터베이스에서 사용되는 유저의 id값
                List<String> authorities = claims.get("authorities", List.class);

                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                UsernamePasswordAuthenticationToken authenticationToken;
                if (authorities.get(0).equals(CatsnapAuthority.MEMBER.name())) {
                    grantedAuthorities.add(CatsnapAuthority.MEMBER);
                } else if (authorities.get(0).equals(CatsnapAuthority.PHOTOGRAPHER.name())) {
                    grantedAuthorities.add(CatsnapAuthority.PHOTOGRAPHER);
                }
                authenticationToken = new MemberAuthenticationToken(identifier, null,
                    grantedAuthorities,
                    id);

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
                //todo : refresh token을 추출하고 검증하는 로직 추가
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
