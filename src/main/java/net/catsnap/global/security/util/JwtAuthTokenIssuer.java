package net.catsnap.global.security.util;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import net.catsnap.global.security.dto.AuthTokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthTokenIssuer implements AuthTokenIssuer {

    private final Long accessTokenExpirationMinutes;
    private final Long refreshTokenExpirationMinutes;
    private final SecretKey key;

    public JwtAuthTokenIssuer(
        @Value("${spring.security.expiration.access-token}")
        Long accessTokenExpirationMinutes,
        @Value("${spring.security.expiration.refresh-token}")
        Long refreshTokenExpirationMinutes,
        SecretKey key) {
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
        this.key = key;
    }

    @Override
    public AuthTokenDTO issueAuthToken(Authentication authentication) {
        String accessToken = issueJwtToken(
            authentication,
            "accessToken",
            accessTokenExpirationMinutes
        );
        String refreshToken = issueJwtToken(
            authentication,
            "refreshToken",
            refreshTokenExpirationMinutes
        );
        return AuthTokenDTO.of(accessToken, refreshToken);
    }

    private String issueJwtToken(Authentication authentication, String type,
        Long expirationMinutes) {
        return Jwts.builder()
            .setHeader(Map.of(
                "provider", "catsnap",
                "type", type
            ))
            .setClaims(Map.of(
                "identifier", authentication.getPrincipal(),
                "authorities", authentication.getAuthorities(),
                "id", authentication.getDetails()
            ))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60L * 1000L))
            .signWith(key)
            .compact();

    }
}
