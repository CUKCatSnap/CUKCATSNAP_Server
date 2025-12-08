package net.catsnap.CatsnapGateway.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtTokenParserTest {

    @Spy
    private SecretKey secretKey = Keys.hmacShaKeyFor(
        "testSecretKeyForJwtTokenParserTestThatIsLongEnough1234567890".getBytes(
            StandardCharsets.UTF_8));

    @InjectMocks
    private JwtTokenParser jwtTokenParser;

    @Test
    void 유효한_JWT_토큰을_파싱하여_Claims를_반환한다() {
        // given
        String token = Jwts.builder()
            .claim("id", 1L)
            .claim("authorities", List.of("MEMBER"))
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();

        // when
        Optional<Claims> result = jwtTokenParser.parseClaims(token);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().get("id", Long.class)).isEqualTo(1L);
        assertThat(result.get().get("authorities", List.class)).containsExactly("MEMBER");
    }

    @Test
    void 만료된_JWT_토큰은_빈_Optional을_반환한다() {
        // given
        String expiredToken = Jwts.builder()
            .claim("id", 1L)
            .claim("authorities", List.of("MEMBER"))
            .setExpiration(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();

        // when
        Optional<Claims> result = jwtTokenParser.parseClaims(expiredToken);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 잘못된_서명의_JWT_토큰은_빈_Optional을_반환한다() {
        // given
        SecretKey wrongKey = Keys.hmacShaKeyFor(
            "wrongSecretKeyForJwtTokenParserTestThatIsLongEnough1234567890".getBytes(
                StandardCharsets.UTF_8));
        String tokenWithWrongSignature = Jwts.builder()
            .claim("id", 1L)
            .claim("authorities", List.of("MEMBER"))
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .signWith(wrongKey)
            .compact();

        // when
        Optional<Claims> result = jwtTokenParser.parseClaims(tokenWithWrongSignature);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 형식이_잘못된_토큰은_빈_Optional을_반환한다() {
        // given
        String malformedToken = "this.is.not.a.valid.jwt.token";

        // when
        Optional<Claims> result = jwtTokenParser.parseClaims(malformedToken);

        // then
        assertThat(result).isEmpty();
    }
}
