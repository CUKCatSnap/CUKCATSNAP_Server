package net.catsnap.CatsnapReservation.program.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.application.dto.request.ProgramCreateRequest;
import net.catsnap.CatsnapReservation.program.application.dto.response.ProgramResponse;
import net.catsnap.CatsnapReservation.program.infrastructure.repository.ProgramRepository;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("ProgramService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProgramServiceTest {

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramRepository programRepository;

    @AfterEach
    void cleanup() {
        programRepository.deleteAll();
    }

    @Nested
    class 프로그램_생성_통합_테스트 {

        @Test
        @Transactional
        void 프로그램_생성에_성공한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅 촬영",
                "아름다운 웨딩 스냅을 촬영해드립니다.",
                150000L,
                90
            );

            // when
            ProgramResponse response = programService.createProgram(photographerId, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isNotNull();
        }

        @Test
        @Transactional
        void 설명_없이_프로그램_생성에_성공한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "프로필 촬영",
                null,
                100000L,
                60
            );

            // when
            ProgramResponse response = programService.createProgram(photographerId, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isNotNull();
        }

        @Test
        @Transactional
        void 무료_프로그램_생성에_성공한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "무료 상담",
                "무료로 상담해드립니다.",
                0L,
                30
            );

            // when
            ProgramResponse response = programService.createProgram(photographerId, request);

            // then
            assertThat(response.id()).isNotNull();
        }

        @Test
        @Transactional
        void 프로그램_생성_시_DB에_저장된다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "스튜디오 촬영",
                "스튜디오에서 촬영합니다.",
                200000L,
                120
            );

            // when
            ProgramResponse response = programService.createProgram(photographerId, request);

            // then
            Program savedProgram = programRepository.findById(response.id()).orElseThrow();
            assertThat(savedProgram.getPhotographerId()).isEqualTo(photographerId);
            assertThat(savedProgram.getTitle().getValue()).isEqualTo("스튜디오 촬영");
            assertThat(savedProgram.isDeleted()).isFalse();
        }

        @Test
        void 빈_제목으로_생성_시_예외가_발생한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "",
                "설명",
                100000L,
                60
            );

            // when & then
            assertThatThrownBy(() -> programService.createProgram(photographerId, request))
                .isInstanceOf(DomainException.class);
        }

        @Test
        void 음수_가격으로_생성_시_예외가_발생한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "테스트",
                "설명",
                -1L,
                60
            );

            // when & then
            assertThatThrownBy(() -> programService.createProgram(photographerId, request))
                .isInstanceOf(DomainException.class);
        }

        @Test
        void 영분_소요시간으로_생성_시_예외가_발생한다() {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "테스트",
                "설명",
                100000L,
                0
            );

            // when & then
            assertThatThrownBy(() -> programService.createProgram(photographerId, request))
                .isInstanceOf(DomainException.class);
        }
    }
}
