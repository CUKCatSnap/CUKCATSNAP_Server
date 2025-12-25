package net.catsnap.CatsnapGateway.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("JwtTokenParser 테스트")
class JwtTokenParserTest {

    private SecretKey secretKey;
    private JwtTokenParser jwtTokenParser;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(
            "testSecretKeyForJwtTokenParserTestThatIsLongEnough1234567890".getBytes(
                StandardCharsets.UTF_8));
        jwtTokenParser = new JwtTokenParser(secretKey);
    }

    @Nested
    @DisplayName("유효한 토큰 파싱 시")
    class ParseValidToken {

        @Test
        @DisplayName("유효한 JWT 토큰을 파싱하여 TokenClaims를 반환한다")
        void parseValidToken() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "PHOTOGRAPHER"
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().userId()).isEqualTo(1L);
            assertThat(result.get().authority()).isEqualTo("PHOTOGRAPHER");
        }

        @Test
        @DisplayName("MODEL 권한을 가진 토큰을 파싱한다")
        void parseTokenWithModelAuthority() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 2L,
                    "authority", "MODEL"
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().userId()).isEqualTo(2L);
            assertThat(result.get().authority()).isEqualTo("MODEL");
        }

        @Test
        @DisplayName("헤더가 포함된 토큰을 파싱한다")
        void parseTokenWithHeaders() {
            // given
            String tokenValue = Jwts.builder()
                .header()
                .add("provider", "catsnap")
                .add("type", "accessToken")
                .and()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "PHOTOGRAPHER"
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().userId()).isEqualTo(1L);
            assertThat(result.get().authority()).isEqualTo("PHOTOGRAPHER");
        }
    }

    @Nested
    @DisplayName("만료된 토큰 파싱 시")
    class ParseExpiredToken {

        @Test
        @DisplayName("만료된 JWT 토큰은 빈 Optional을 반환한다")
        void parseExpiredToken() {
            // given
            String expiredTokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "PHOTOGRAPHER"
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(expiredTokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("잘못된 서명의 토큰 파싱 시")
    class ParseTokenWithWrongSignature {

        @Test
        @DisplayName("잘못된 서명의 JWT 토큰은 빈 Optional을 반환한다")
        void parseTokenWithWrongSignature() {
            // given
            SecretKey wrongKey = Keys.hmacShaKeyFor(
                "wrongSecretKeyForJwtTokenParserTestThatIsLongEnough1234567890".getBytes(
                    StandardCharsets.UTF_8));
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "PHOTOGRAPHER"
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(wrongKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("형식이 잘못된 토큰 파싱 시")
    class ParseMalformedToken {

        @Test
        @DisplayName("형식이 잘못된 토큰은 빈 Optional을 반환한다")
        void parseMalformedToken() {
            // given
            Token malformedToken = new Token("this.is.not.a.valid.jwt.token");

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(malformedToken);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("필수 클레임이 없는 토큰 파싱 시")
    class ParseTokenWithMissingClaims {

        @Test
        @DisplayName("userId가 없는 토큰은 빈 Optional을 반환한다")
        void parseTokenWithoutUserId() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of("authority", "PHOTOGRAPHER"))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("authority가 없는 토큰은 빈 Optional을 반환한다")
        void parseTokenWithoutAuthority() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of("id", 1L))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("authority가 빈 문자열인 토큰은 빈 Optional을 반환한다")
        void parseTokenWithEmptyAuthority() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", ""
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("authority가 공백만 있는 토큰은 빈 Optional을 반환한다")
        void parseTokenWithBlankAuthority() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "   "
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
            Token token = new Token(tokenValue);

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(token);

            // then
            assertThat(result).isEmpty();
        }
    }
}
