package net.catsnap.CatsnapGateway.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PassportServiceTest {

    private final PassportService passportService = new PassportService();

    @Test
    void 사용자_인증_정보를_기반으로_여권_헤더를_발행한다() {
        // given
        UserAuthInformation userAuthInformation = new UserAuthInformation(1L, "MEMBER");
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();

        // when
        ServerHttpRequest result = passportService.issuePassport(request, userAuthInformation);

        // then
        assertThat(result.getHeaders().getFirst("X-User-Id")).isEqualTo("1");
        assertThat(result.getHeaders().getFirst("X-Authority")).isEqualTo("MEMBER");
    }

    @Test
    void 기존의_여권_헤더를_무효화한다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header("X-User-Id", "1")
            .header("X-Authority", "MEMBER")
            .build();

        // when
        ServerHttpRequest result = passportService.invalidatePassport(request);

        // then
        assertThat(result.getHeaders().get("X-User-Id")).isNull();
        assertThat(result.getHeaders().get("X-Authority")).isNull();
    }

    @Test
    void 여권이_없더라도_널처리가_된다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .build();

        // when
        ServerHttpRequest result = passportService.invalidatePassport(request);

        // then
        assertThat(result.getHeaders().get("X-User-Id")).isNull();
        assertThat(result.getHeaders().get("X-Authority")).isNull();
    }
}