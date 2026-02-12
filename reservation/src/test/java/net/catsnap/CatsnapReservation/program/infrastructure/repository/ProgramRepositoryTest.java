package net.catsnap.CatsnapReservation.program.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.fixture.ProgramFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DisplayName("ProgramRepository 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@EnableJpaAuditing
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Test
    void 프로그램을_저장하고_조회할_수_있다() {
        // given
        Program program = ProgramFixture.createDefault();

        // when
        Program saved = programRepository.save(program);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhotographerId()).isEqualTo(ProgramFixture.DEFAULT_PHOTOGRAPHER_ID);
    }

    @Test
    void isActive_Specification으로_삭제되지_않은_프로그램만_조회된다() {
        // given
        Program activeProgram1 = ProgramFixture.createWithPhotographerId(1L);
        Program activeProgram2 = ProgramFixture.createWithPhotographerId(2L);
        Program deletedProgram = ProgramFixture.createDefaultDeleted();

        programRepository.saveAll(List.of(activeProgram1, activeProgram2, deletedProgram));

        // when
        List<Program> result = programRepository.findAll(ProgramSpecification.isActive());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(program -> !program.isDeleted());
    }

    @Test
    void isDeleted_Specification으로_삭제된_프로그램만_조회된다() {
        // given
        Program activeProgram = ProgramFixture.createDefault();
        Program deletedProgram1 = ProgramFixture.createDeleted(1L);
        Program deletedProgram2 = ProgramFixture.createDeleted(2L);

        programRepository.saveAll(List.of(activeProgram, deletedProgram1, deletedProgram2));

        // when
        List<Program> result = programRepository.findAll(ProgramSpecification.isDeleted());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Program::isDeleted);
    }

    @Test
    void belongsTo_Specification으로_특정_작가의_프로그램만_조회된다() {
        // given
        Long targetPhotographerId = 100L;
        Program program1 = ProgramFixture.createWithPhotographerId(targetPhotographerId);
        Program program2 = ProgramFixture.createWithPhotographerId(targetPhotographerId);
        Program otherProgram = ProgramFixture.createWithPhotographerId(200L);

        programRepository.saveAll(List.of(program1, program2, otherProgram));

        // when
        List<Program> result = programRepository.findAll(
            ProgramSpecification.belongsTo(targetPhotographerId)
        );

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(program -> program.isOwnedBy(targetPhotographerId));
    }

    @Test
    void Specification을_조합하여_복합_조건_조회가_가능하다() {
        // given
        Long targetPhotographerId = 100L;

        Program activeProgram = ProgramFixture.createWithPhotographerId(targetPhotographerId);
        Program deletedProgram = ProgramFixture.createDeleted(targetPhotographerId);
        Program otherActiveProgram = ProgramFixture.createWithPhotographerId(200L);

        programRepository.saveAll(List.of(activeProgram, deletedProgram, otherActiveProgram));

        // when - 특정 작가의 활성 프로그램만 조회
        List<Program> result = programRepository.findAll(
            ProgramSpecification.belongsTo(targetPhotographerId)
                .and(ProgramSpecification.isActive())
        );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isOwnedBy(targetPhotographerId)).isTrue();
        assertThat(result.get(0).isDeleted()).isFalse();
    }

    @Test
    void or_조건으로_여러_작가의_프로그램을_조회할_수_있다() {
        // given
        Long photographerId1 = 100L;
        Long photographerId2 = 200L;

        Program program1 = ProgramFixture.createWithPhotographerId(photographerId1);
        Program program2 = ProgramFixture.createWithPhotographerId(photographerId2);
        Program program3 = ProgramFixture.createWithPhotographerId(300L);

        programRepository.saveAll(List.of(program1, program2, program3));

        // when
        List<Program> result = programRepository.findAll(
            ProgramSpecification.belongsTo(photographerId1)
                .or(ProgramSpecification.belongsTo(photographerId2))
        );

        // then
        assertThat(result).hasSize(2);
        assertThat(result).anyMatch(program -> program.isOwnedBy(photographerId1));
        assertThat(result).anyMatch(program -> program.isOwnedBy(photographerId2));
    }
}