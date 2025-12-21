package net.catsnap.CatsnapAuthorization.model.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelLoginRequest;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import net.catsnap.CatsnapAuthorization.model.dto.response.TokenResponse;
import net.catsnap.CatsnapAuthorization.model.infrastructure.ModelRepository;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSessionRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
@DisplayName("ModelService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelServiceTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
        .withExposedPorts(6379);

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    void cleanup() {
        // 테스트 격리를 위해 데이터 정리
        transactionTemplate.executeWithoutResult(status -> {
            loginSessionRepository.deleteAll();
            modelRepository.deleteAll();
        });
    }

    @Test
    @Transactional
    void 회원가입을_성공한다() {
        //given
        ModelSignUpRequest request = new ModelSignUpRequest(
            "newuser",
            "password1234",
            "새유저",
            LocalDate.of(1995, 5, 15),
            "010-1234-5678"
        );

        //when
        assertThatCode(() -> modelService.signUp(request))
            .doesNotThrowAnyException();

        //then
        Model savedModel = modelRepository.findAll().get(0);
        assertThat(savedModel).isNotNull();
        assertThat(savedModel.getIdentifier().getValue()).isEqualTo("newuser");
        assertThat(savedModel.getNickname().getValue()).isEqualTo("새유저");
    }

    @Test
    void 아이디가_겹쳐_회원가입에_실패한다() {
        //given
        ModelSignUpRequest firstRequest = new ModelSignUpRequest(
            "duplicateuser",
            "password1234",
            "첫번째유저",
            LocalDate.of(1990, 1, 1),
            "010-1111-1111"
        );

        // 첫 번째 가입을 별도 트랜잭션에서 실행하고 커밋 (운영 환경 시뮬레이션)
        transactionTemplate.executeWithoutResult(status -> {
            modelService.signUp(firstRequest);
        });

        ModelSignUpRequest duplicateRequest = new ModelSignUpRequest(
            "duplicateuser",
            "password5678",
            "두번째유저",
            LocalDate.of(1995, 5, 5),
            "010-2222-2222"
        );

        //when & then
        // 두 번째 가입은 새로운 트랜잭션에서 실행됨
        assertThatThrownBy(() -> modelService.signUp(duplicateRequest))
            .isInstanceOf(BusinessException.class)
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);

        // cleanup()에서 데이터 정리됨
    }

    @Test
    void 올바른_정보로_로그인에_성공한다() {
        // given
        String identifier = "loginuser";
        String password = "password1234";

        // 먼저 회원가입
        ModelSignUpRequest signUpRequest = new ModelSignUpRequest(
            identifier,
            password,
            "로그인유저",
            LocalDate.of(1990, 1, 1),
            "010-1234-5678"
        );
        transactionTemplate.executeWithoutResult(status -> {
            modelService.signUp(signUpRequest);
        });

        ModelLoginRequest loginRequest = new ModelLoginRequest(identifier, password);

        // when
        TokenResponse tokenResponse = modelService.login(loginRequest);

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
        ModelLoginRequest loginRequest = new ModelLoginRequest("nonexistent", "password1234");

        // when & then
        assertThatThrownBy(() -> modelService.login(loginRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("존재하지 않는 사용자입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 잘못된_비밀번호로_로그인_시_예외가_발생한다() {
        // given
        String identifier = "testuser";
        String correctPassword = "password1234";
        String wrongPassword = "wrongpassword";

        // 회원가입
        ModelSignUpRequest signUpRequest = new ModelSignUpRequest(
            identifier,
            correctPassword,
            "테스트유저",
            LocalDate.of(1990, 1, 1),
            "010-1234-5678"
        );
        transactionTemplate.executeWithoutResult(status -> {
            modelService.signUp(signUpRequest);
        });

        ModelLoginRequest loginRequest = new ModelLoginRequest(identifier, wrongPassword);

        // when & then
        assertThatThrownBy(() -> modelService.login(loginRequest))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("비밀번호가 일치하지 않습니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);

        // LoginSession이 생성되지 않았는지 확인
        assertThat(loginSessionRepository.count()).isEqualTo(0);
    }

    @Test
    void 로그인_성공_시_액세스_토큰과_세션_키가_반환된다() {
        // given
        String identifier = "tokenuser";
        String password = "password1234";

        ModelSignUpRequest signUpRequest = new ModelSignUpRequest(
            identifier,
            password,
            "토큰유저",
            LocalDate.of(1995, 5, 15),
            "010-9999-8888"
        );
        transactionTemplate.executeWithoutResult(status -> {
            modelService.signUp(signUpRequest);
        });

        ModelLoginRequest loginRequest = new ModelLoginRequest(identifier, password);

        // when
        TokenResponse tokenResponse = modelService.login(loginRequest);

        // then
        // 액세스 토큰이 JWT 형식인지 확인 (헤더.페이로드.서명)
        assertThat(tokenResponse.accessToken().split("\\.")).hasSize(3);

        // 세션 키가 UUID 형식인지 확인
        assertThat(tokenResponse.refreshToken())
            .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }
}