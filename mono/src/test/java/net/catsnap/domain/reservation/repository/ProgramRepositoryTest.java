package net.catsnap.domain.reservation.repository;


import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.support.fixture.PhotographerFixture;
import net.catsnap.support.fixture.ProgramFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Test
    void 작가의_삭제되지_않은_프로그램만_조회() {
        //given
        Photographer photographer = PhotographerFixture.photographer().build();
        photographerRepository.save(photographer);
        Program program1 = ProgramFixture.program()
            .photographer(photographer)
            .deleted(false)
            .build();
        Program program2 = ProgramFixture.program()
            .photographer(photographer)
            .deleted(false)
            .build();
        Program program3 = ProgramFixture.program()
            .photographer(photographer)
            .deleted(true)
            .build();
        photographerRepository.save(photographer);
        programRepository.save(program1);
        programRepository.save(program2);
        programRepository.save(program3);

        //when
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographer.getId());

        //then
        Assertions.assertThat(programList).hasSize(2);
    }
}