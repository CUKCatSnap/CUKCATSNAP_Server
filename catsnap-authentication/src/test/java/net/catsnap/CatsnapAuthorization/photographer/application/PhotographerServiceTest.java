package net.catsnap.CatsnapAuthorization.photographer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.domain.OutboxStatus;
import net.catsnap.CatsnapAuthorization.event.infrastructure.OutboxRepository;
import net.catsnap.CatsnapAuthorization.model.dto.response.TokenResponse;
import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerLoginRequest;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerSignUpRequest;
import net.catsnap.CatsnapAuthorization.photographer.infrastructure.PhotographerRepository;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSessionRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@DisplayName("PhotographerService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerServiceTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
        .withExposedPorts(6379);

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    void cleanup() {
        // 테스트 격리를 위해 데이터 정리
        transactionTemplate.executeWithoutResult(status -> {
            loginSessionRepository.deleteAll();
            photographerRepository.deleteAll();
            outboxRepository.deleteAll();
        });
    }

    @Nested
    class 회원가입_통합_테스트 {

        @Test
        @Transactional
        void 회원가입을_성공한다() {
            //given
            PhotographerSignUpRequest request = new PhotographerSignUpRequest(
                "newphotographer",
                "password1234",
                "김작가",
                "010-1234-5678"
            );

            //when
            assertThatCode(() -> photographerService.signUp(request))
                .doesNotThrowAnyException();

            //then
            Photographer savedPhotographer = photographerRepository.findAll().get(0);
            assertThat(savedPhotographer).isNotNull();
            assertThat(savedPhotographer.getIdentifier().getValue()).isEqualTo("newphotographer");
            assertThat(savedPhotographer.getName().getValue()).isEqualTo("김작가");
        }

        @Test
        void 회원가입_성공_시_PhotographerCreated_이벤트가_발행된다() {
            // given
            PhotographerSignUpRequest request = new PhotographerSignUpRequest(
                "eventphotographer",
                "password1234",
                "이벤트작가",
                "010-5555-6666"
            );

            // when
            transactionTemplate.executeWithoutResult(status -> {
                photographerService.signUp(request);
            });

            // then
            List<Outbox> pendingEvents = outboxRepository.findByStatus(OutboxStatus.PENDING);
            assertThat(pendingEvents).hasSize(1);
            assertThat(pendingEvents.get(0).getAggregateType()).isEqualTo("Photographer");
            assertThat(pendingEvents.get(0).getEventType()).isEqualTo("PhotographerCreated");
        }

        @Test
        void 아이디가_겹쳐_회원가입에_실패한다() {
            //given
            PhotographerSignUpRequest firstRequest = new PhotographerSignUpRequest(
                "photographer",
                "password1234",
                "첫번째작가",
                "010-1111-1111"
            );

            // 첫 번째 가입을 별도 트랜잭션에서 실행하고 커밋 (운영 환경 시뮬레이션)
            transactionTemplate.executeWithoutResult(status -> {
                photographerService.signUp(firstRequest);
            });

            PhotographerSignUpRequest duplicateRequest = new PhotographerSignUpRequest(
                "photographer",
                "password5678",
                "두번째작가",
                "010-2222-2222"
            );

            //when & then
            // 두 번째 가입은 새로운 트랜잭션에서 실행됨
            assertThatThrownBy(() -> photographerService.signUp(duplicateRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("resultCode")
                .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);

            // cleanup()에서 데이터 정리됨
        }
    }

    @Nested
    class 로그인_통합_테스트 {

        @Test
        void 올바른_정보로_로그인에_성공한다() {
            // given
            String identifier = "loginphotographer";
            String password = "password1234";

            // 먼저 회원가입
            PhotographerSignUpRequest signUpRequest = new PhotographerSignUpRequest(
                identifier,
                password,
                "로그인작가",
                "010-1234-5678"
            );
            transactionTemplate.executeWithoutResult(status -> {
                photographerService.signUp(signUpRequest);
            });

            PhotographerLoginRequest loginRequest = new PhotographerLoginRequest(identifier,
                password);

            // when
            TokenResponse tokenResponse = photographerService.login(loginRequest);

            // then
            assertThat(tokenResponse).isNotNull();
            assertThat(tokenResponse.accessToken()).isNotNull();
            assertThat(tokenResponse.accessToken()).isNotEmpty();
            assertThat(tokenResponse.refreshToken()).isNotNull();
            assertThat(tokenResponse.refreshToken()).isNotEmpty();

            // LoginSession이 저장되었는지 확인
            assertThat(loginSessionRepository.count()).isEqualTo(1);
        }

        @Test
        void 존재하지_않는_사용자로_로그인_시_예외가_발생한다() {
            // given
            PhotographerLoginRequest loginRequest = new PhotographerLoginRequest("nonexistent",
                "password1234");

            // when & then
            assertThatThrownBy(() -> photographerService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("아이디 또는 비밀번호가 존재하지 않는 사용자입니다.")
                .extracting("resultCode")
                .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
        }

        @Test
        void 잘못된_비밀번호로_로그인_시_예외가_발생한다() {
            // given
            String identifier = "testphotographer";
            String correctPassword = "password1234";
            String wrongPassword = "wrongpassword";

            // 회원가입
            PhotographerSignUpRequest signUpRequest = new PhotographerSignUpRequest(
                identifier,
                correctPassword,
                "테스트작가",
                "010-1234-5678"
            );
            transactionTemplate.executeWithoutResult(status -> {
                photographerService.signUp(signUpRequest);
            });

            PhotographerLoginRequest loginRequest = new PhotographerLoginRequest(identifier,
                wrongPassword);

            // when & then
            assertThatThrownBy(() -> photographerService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("아이디 또는 비밀번호가 존재하지 않는 사용자입니다.")
                .extracting("resultCode")
                .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);

            // LoginSession이 생성되지 않았는지 확인
            assertThat(loginSessionRepository.count()).isEqualTo(0);
        }

        @Test
        void 로그인_성공_시_액세스_토큰과_세션_키가_반환된다() {
            // given
            String identifier = "tokenphotographer";
            String password = "password1234";

            PhotographerSignUpRequest signUpRequest = new PhotographerSignUpRequest(
                identifier,
                password,
                "토큰작가",
                "010-9999-8888"
            );
            transactionTemplate.executeWithoutResult(status -> {
                photographerService.signUp(signUpRequest);
            });

            PhotographerLoginRequest loginRequest = new PhotographerLoginRequest(identifier,
                password);

            // when
            TokenResponse tokenResponse = photographerService.login(loginRequest);

            // then
            // 액세스 토큰이 JWT 형식인지 확인 (헤더.페이로드.서명)
            assertThat(tokenResponse.accessToken().split("\\.")).hasSize(3);

            // 세션 키가 UUID 형식인지 확인
            assertThat(tokenResponse.refreshToken())
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        }
    }
}