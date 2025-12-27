package net.catsnap.CatsnapAuthorization.photographer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerSignUpRequest;
import net.catsnap.CatsnapAuthorization.photographer.infrastructure.PhotographerRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@DisplayName("PhotographerService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerServiceTest {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    void cleanup() {
        // 테스트 격리를 위해 데이터 정리
        transactionTemplate.executeWithoutResult(status -> {
            photographerRepository.deleteAll();
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
}
