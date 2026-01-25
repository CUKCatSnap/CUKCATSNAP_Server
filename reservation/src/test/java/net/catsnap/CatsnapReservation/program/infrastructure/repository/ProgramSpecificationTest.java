package net.catsnap.CatsnapReservation.program.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.program.domain.Program;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

@DisplayName("ProgramSpecification 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProgramSpecificationTest {

    @Test
    void isActive_Specification이_생성된다() {
        // when
        Specification<Program> spec = ProgramSpecification.isActive();

        // then
        assertThat(spec).isNotNull();
    }

    @Test
    void isDeleted_Specification이_생성된다() {
        // when
        Specification<Program> spec = ProgramSpecification.isDeleted();

        // then
        assertThat(spec).isNotNull();
    }

    @Test
    void belongsTo_Specification이_생성된다() {
        // when
        Specification<Program> spec = ProgramSpecification.belongsTo(1L);

        // then
        assertThat(spec).isNotNull();
    }

    @Test
    void Specification을_조합할_수_있다() {
        // when
        Specification<Program> spec = ProgramSpecification.isActive()
            .and(ProgramSpecification.belongsTo(1L));

        // then
        assertThat(spec).isNotNull();
    }

    @Test
    void or_조건으로_Specification을_조합할_수_있다() {
        // when
        Specification<Program> spec = ProgramSpecification.belongsTo(1L)
            .or(ProgramSpecification.belongsTo(2L));

        // then
        assertThat(spec).isNotNull();
    }
}
