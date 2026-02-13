package net.catsnap.CatsnapGateway.passport.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PassportIssuerTest {

    @Mock
    private PassportHandler passportHandler;

    @InjectMocks
    private PassportIssuer passportIssuer;

    @Nested
    class Passport_발급_시 {

        @Test
        void 사진작가_권한으로_서명된_Passport를_발급한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Long userId = 1L;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;
            String signedPassport = "signed-passport-base64-string";

            given(passportHandler.sign(any(Passport.class))).willReturn(signedPassport);

            // when
            ServerHttpRequest result = passportIssuer.issuePassport(request, userId, authority);

            // then
            assertThat(result.getHeaders().getFirst("X-Passport")).isEqualTo(signedPassport);

            // Passport가 올바르게 생성되었는지 검증
            ArgumentCaptor<Passport> passportCaptor = ArgumentCaptor.forClass(Passport.class);
            verify(passportHandler).sign(passportCaptor.capture());

            Passport capturedPassport = passportCaptor.getValue();
            assertThat(capturedPassport.userId()).isEqualTo(userId);
            assertThat(capturedPassport.authority()).isEqualTo(authority);
            assertThat(capturedPassport.version()).isEqualTo((byte) 1);
            assertThat(capturedPassport.iat()).isNotNull();
            assertThat(capturedPassport.exp()).isNotNull();
            assertThat(capturedPassport.exp()).isAfter(capturedPassport.iat());
        }

        @Test
        void 모델_권한으로_서명된_Passport를_발급한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Long userId = 2L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            String signedPassport = "signed-passport-model";

            given(passportHandler.sign(any(Passport.class))).willReturn(signedPassport);

            // when
            ServerHttpRequest result = passportIssuer.issuePassport(request, userId, authority);

            // then
            assertThat(result.getHeaders().getFirst("X-Passport")).isEqualTo(signedPassport);

            ArgumentCaptor<Passport> passportCaptor = ArgumentCaptor.forClass(Passport.class);
            verify(passportHandler).sign(passportCaptor.capture());

            Passport capturedPassport = passportCaptor.getValue();
            assertThat(capturedPassport.userId()).isEqualTo(userId);
            assertThat(capturedPassport.authority()).isEqualTo(CatsnapAuthority.MODEL);
        }

        @Test
        void 익명_사용자로_서명된_Passport를_발급한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Long userId = -1L;
            CatsnapAuthority authority = CatsnapAuthority.ANONYMOUS;
            String signedPassport = "signed-passport-anonymous";

            given(passportHandler.sign(any(Passport.class))).willReturn(signedPassport);

            // when
            ServerHttpRequest result = passportIssuer.issuePassport(request, userId, authority);

            // then
            assertThat(result.getHeaders().getFirst("X-Passport")).isEqualTo(signedPassport);

            ArgumentCaptor<Passport> passportCaptor = ArgumentCaptor.forClass(Passport.class);
            verify(passportHandler).sign(passportCaptor.capture());

            Passport capturedPassport = passportCaptor.getValue();
            assertThat(capturedPassport.userId()).isEqualTo(-1L);
            assertThat(capturedPassport.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
        }

        @Test
        void 기존_Passport_헤더가_있으면_무효화하고_새로운_Passport를_발급한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("X-Passport", "fake-passport-injected-by-user")
                .build();
            Long userId = 1L;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;
            String newSignedPassport = "real-signed-passport";

            given(passportHandler.sign(any(Passport.class))).willReturn(newSignedPassport);

            // when
            ServerHttpRequest result = passportIssuer.issuePassport(request, userId, authority);

            // then
            // 새로운 Passport로 교체되었는지 확인
            assertThat(result.getHeaders().getFirst("X-Passport")).isEqualTo(newSignedPassport);
            assertThat(result.getHeaders().getFirst("X-Passport")).isNotEqualTo(
                "fake-passport-injected-by-user");
        }

        @Test
        void 다른_헤더는_유지하면서_Passport만_추가한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer jwt-token")
                .header("Content-Type", "application/json")
                .build();
            Long userId = 1L;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;
            String signedPassport = "signed-passport";

            given(passportHandler.sign(any(Passport.class))).willReturn(signedPassport);

            // when
            ServerHttpRequest result = passportIssuer.issuePassport(request, userId, authority);

            // then
            assertThat(result.getHeaders().getFirst("X-Passport")).isEqualTo(signedPassport);
            assertThat(result.getHeaders().getFirst("Authorization")).isEqualTo("Bearer jwt-token");
            assertThat(result.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");
        }
    }

    @Nested
    class Passport_무효화_시 {

        @Test
        void X_Passport_헤더를_제거한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("X-Passport", "fake-passport")
                .build();

            // when
            ServerHttpRequest result = passportIssuer.invalidatePassport(request);

            // then
            assertThat(result.getHeaders().containsKey("X-Passport")).isFalse();
        }

        @Test
        void X_Passport_헤더가_없어도_정상_동작한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();

            // when
            ServerHttpRequest result = passportIssuer.invalidatePassport(request);

            // then
            assertThat(result.getHeaders().containsKey("X-Passport")).isFalse();
        }

        @Test
        void 다른_헤더는_유지한다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("X-Passport", "fake-passport")
                .header("Authorization", "Bearer jwt-token")
                .header("Content-Type", "application/json")
                .build();

            // when
            ServerHttpRequest result = passportIssuer.invalidatePassport(request);

            // then
            assertThat(result.getHeaders().containsKey("X-Passport")).isFalse();
            assertThat(result.getHeaders().getFirst("Authorization")).isEqualTo("Bearer jwt-token");
            assertThat(result.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");
        }
    }
}