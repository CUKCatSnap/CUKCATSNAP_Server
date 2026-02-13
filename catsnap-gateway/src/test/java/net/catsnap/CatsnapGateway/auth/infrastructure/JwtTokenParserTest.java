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
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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
    class 유효한_토큰_파싱_시 {

        @Test
        void 유효한_JWT_토큰을_파싱하여_TokenClaims를_반환한다() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "photographer"
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
            assertThat(result.get().authority()).isEqualTo(CatsnapAuthority.PHOTOGRAPHER);
        }

        @Test
        void MODEL_권한을_가진_토큰을_파싱한다() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 2L,
                    "authority", "model"
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
            assertThat(result.get().authority()).isEqualTo(CatsnapAuthority.MODEL);
        }

        @Test
        void 헤더가_포함된_토큰을_파싱한다() {
            // given
            String tokenValue = Jwts.builder()
                .header()
                .add("provider", "catsnap")
                .add("type", "accessToken")
                .and()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "photographer"
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
            assertThat(result.get().authority()).isEqualTo(CatsnapAuthority.PHOTOGRAPHER);
        }
    }

    @Nested
    class 만료된_토큰_파싱_시 {

        @Test
        void 만료된_JWT_토큰은_빈_Optional을_반환한다() {
            // given
            String expiredTokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "photographer"
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
    class 잘못된_서명_토큰_파싱_시 {

        @Test
        void 잘못된_서명의_JWT_토큰은_빈_Optional을_반환한다() {
            // given
            SecretKey wrongKey = Keys.hmacShaKeyFor(
                "wrongSecretKeyForJwtTokenParserTestThatIsLongEnough1234567890".getBytes(
                    StandardCharsets.UTF_8));
            String tokenValue = Jwts.builder()
                .claims(Map.of(
                    "id", 1L,
                    "authority", "photographer"
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
    class 형식_잘못된_토큰_파싱_시 {

        @Test
        void 형식이_잘못된_토큰은_빈_Optional을_반환한다() {
            // given
            Token malformedToken = new Token("this.is.not.a.valid.jwt.token");

            // when
            Optional<TokenClaims> result = jwtTokenParser.parse(malformedToken);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class 필수_클레임_없는_토큰_파싱_시 {

        @Test
        void userId가_없는_토큰은_빈_Optional을_반환한다() {
            // given
            String tokenValue = Jwts.builder()
                .claims(Map.of("authority", "photographer"))
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
        void authority가_없는_토큰은_빈_Optional을_반환한다() {
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
        void authority가_빈_문자열이면_빈_Optional을_반환한다() {
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
        void authority가_공백이면_빈_Optional을_반환한다() {
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
