package net.catsnap.CatsnapGateway.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtTokenServiceTest {

    @Mock
    private JwtTokenParser jwtTokenParser;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Test
    void 유효한_토큰이_있으면_사용자_정보를_반환한다() {
        // given
        String validToken = "valid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
            .build();

        Claims claims = Jwts.claims();
        claims.put("id", 1L);
        claims.put("authorities", List.of("MEMBER"));
        when(jwtTokenParser.parseClaims(validToken)).thenReturn(Optional.of(claims));

        // when
        UserAuthInformation userAuthInformation = jwtTokenService.getUserAuthInformation(request);

        // then
        assertThat(userAuthInformation.userId()).isEqualTo(1L);
        assertThat(userAuthInformation.authority()).isEqualTo("MEMBER");
    }

    @Test
    void 토큰이_없으면_익명_사용자_정보를_반환한다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();

        // when
        UserAuthInformation userAuthInformation = jwtTokenService.getUserAuthInformation(request);

        // then
        assertThat(userAuthInformation.userId()).isEqualTo(-1L);
        assertThat(userAuthInformation.authority()).isEqualTo("ANONYMOUS");
    }

    @Test
    void 토큰이_Bearer_타입이_아니면_익명_사용자_정보를_반환한다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Basic some.other.token")
            .build();

        // when
        UserAuthInformation userAuthInformation = jwtTokenService.getUserAuthInformation(request);

        // then
        assertThat(userAuthInformation.userId()).isEqualTo(-1L);
        assertThat(userAuthInformation.authority()).isEqualTo("ANONYMOUS");
    }

    @Test
    void 토큰_파싱에_실패하면_익명_사용자_정보를_반환한다() {
        // given
        String invalidToken = "invalid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
            .build();
        when(jwtTokenParser.parseClaims(invalidToken)).thenReturn(Optional.empty());

        // when
        UserAuthInformation userAuthInformation = jwtTokenService.getUserAuthInformation(request);

        // then
        assertThat(userAuthInformation.userId()).isEqualTo(-1L);
        assertThat(userAuthInformation.authority()).isEqualTo("ANONYMOUS");
    }
}
