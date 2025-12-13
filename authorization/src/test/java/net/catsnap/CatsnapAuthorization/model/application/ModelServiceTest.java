package net.catsnap.CatsnapAuthorization.model.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import net.catsnap.CatsnapAuthorization.model.infrastructure.ModelRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@DisplayName("ModelService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelServiceTest {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    void cleanup() {
        // 테스트 격리를 위해 데이터 정리
        transactionTemplate.executeWithoutResult(status -> {
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
}