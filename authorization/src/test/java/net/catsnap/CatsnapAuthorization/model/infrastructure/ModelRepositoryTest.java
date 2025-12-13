package net.catsnap.CatsnapAuthorization.model.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.fixture.ModelTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("ModelRepository 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Test
    void 존재하는_Identifier는_true를_반환한다() {
        // given
        Identifier identifier = new Identifier("testuser");
        Model model = ModelTestFixture.builder()
            .identifier(identifier.getValue())
            .build();
        modelRepository.save(model);

        // when
        boolean exists = modelRepository.existsByIdentifier(identifier);

        // then
        assertTrue(exists);
    }

    @Test
    void 존재하지_않는_Identifier는_false를_반환한다() {
        // given
        Identifier nonExistentIdentifier = new Identifier("nonexistent");

        // when
        boolean exists = modelRepository.existsByIdentifier(nonExistentIdentifier);

        // then
        assertFalse(exists);
    }
}