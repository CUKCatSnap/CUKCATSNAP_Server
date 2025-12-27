package net.catsnap.CatsnapAuthorization.session.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.session.application.dto.response.TokenRefreshResponse;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSession;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSessionRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.presentation.error.SecurityErrorCode;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@DisplayName("SessionService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SessionServiceTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
        .withExposedPorts(6379);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @AfterEach
    void cleanup() {
        // 테스트 격리를 위해 데이터 정리
        loginSessionRepository.deleteAll();
    }

    @Nested
    class 리프레시_토큰_갱신 {

        @Test
        void 유효한_리프레시_토큰으로_액세스_토큰을_재발급한다() {
            // given
            LoginSession loginSession = LoginSession.create(1L, CatsnapAuthority.MODEL);
            loginSessionRepository.save(loginSession);

            String refreshToken = loginSession.getSessionKey();

            // when
            TokenRefreshResponse response = sessionService.refreshAccessToken(refreshToken);

            // then
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isNotNull();
            assertThat(response.accessToken()).isNotEmpty();
        }

        @Test
        void 유효하지_않은_리프레시_토큰으로_예외가_발생한다() {
            // given
            String invalidRefreshToken = "invalid-refresh-token";

            // when & then
            assertThatThrownBy(() -> sessionService.refreshAccessToken(invalidRefreshToken))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("유효하지 않거나 만료된 리프레시 토큰입니다.")
                .extracting("resultCode")
                .isEqualTo(SecurityErrorCode.UNAUTHORIZED);
        }

        @Test
        void 재발급된_토큰이_유효한_JWT_형식이다() {
            // given
            LoginSession loginSession = LoginSession.create(2L, CatsnapAuthority.PHOTOGRAPHER);
            loginSessionRepository.save(loginSession);

            String refreshToken = loginSession.getSessionKey();

            // when
            TokenRefreshResponse response = sessionService.refreshAccessToken(refreshToken);

            // then
            // JWT는 헤더.페이로드.서명 형식
            assertThat(response.accessToken().split("\\.")).hasSize(3);
        }

        @Test
        void 토큰_재발급_시_세션_TTL이_리셋된다() {
            // given
            LoginSession loginSession = LoginSession.create(3L, CatsnapAuthority.MODEL);
            loginSessionRepository.save(loginSession);

            String refreshToken = loginSession.getSessionKey();
            Long expectedTTL = 30L * 24 * 60 * 60;  // 30일

            // when
            sessionService.refreshAccessToken(refreshToken);

            // then
            LoginSession updatedSession = loginSessionRepository.findById(refreshToken)
                .orElseThrow();

            assertThat(updatedSession.getSessionLiveDuration()).isEqualTo(expectedTTL);
        }

        @Test
        void 동일한_리프레시_토큰으로_여러_번_재발급할_수_있다() {
            // given
            LoginSession loginSession = LoginSession.create(6L, CatsnapAuthority.MODEL);
            loginSessionRepository.save(loginSession);

            String refreshToken = loginSession.getSessionKey();

            // when
            TokenRefreshResponse firstResponse = sessionService.refreshAccessToken(refreshToken);
            TokenRefreshResponse secondResponse = sessionService.refreshAccessToken(refreshToken);
            TokenRefreshResponse thirdResponse = sessionService.refreshAccessToken(refreshToken);

            // then
            assertThat(firstResponse.accessToken()).isNotNull();
            assertThat(secondResponse.accessToken()).isNotNull();
            assertThat(thirdResponse.accessToken()).isNotNull();
        }
    }
}