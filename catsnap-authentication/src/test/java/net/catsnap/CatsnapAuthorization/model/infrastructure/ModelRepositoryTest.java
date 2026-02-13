package net.catsnap.CatsnapAuthorization.model.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.fixture.ModelTestFixture;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.persistence.JpaConfiguration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfiguration.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Nested
    class existsByIdentifier_메서드_단위_테스트 {

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

    @Nested
    class findByIdentifier_메서드_단위_테스트 {

        @Test
        void 존재하는_Identifier로_조회하면_Model을_반환한다() {
            // given
            Identifier identifier = new Identifier("findtest");
            Model savedModel = ModelTestFixture.builder()
                .identifier(identifier.getValue())
                .nickname("테스트유저")
                .build();
            modelRepository.save(savedModel);

            // when
            Optional<Model> result = modelRepository.findByIdentifier(identifier);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getIdentifier()).isEqualTo(identifier);
            assertThat(result.get().getNickname().getValue()).isEqualTo("테스트유저");
        }

        @Test
        void findByIdentifier_존재하지_않는_Identifier로_조회하면_빈_Optional을_반환한다() {
            // given
            Identifier nonExistentIdentifier = new Identifier("nonexistent");

            // when
            Optional<Model> result = modelRepository.findByIdentifier(nonExistentIdentifier);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void findByIdentifier_조회된_Model의_모든_필드가_저장된_값과_일치한다() {
            // given
            Identifier identifier = new Identifier("fulltest");
            String nickname = "풀테스트";
            String phoneNumber = "010-9999-8888";
            LocalDate birthday = LocalDate.of(1995, 5, 15);
            String rawPassword = "testpass1234";

            Model savedModel = ModelTestFixture.builder()
                .identifier(identifier.getValue())
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .password(rawPassword)
                .build();
            Model saved = modelRepository.save(savedModel);

            // when
            Optional<Model> result = modelRepository.findByIdentifier(identifier);

            // then
            assertThat(result).isPresent();
            Model foundModel = result.get();
            assertThat(foundModel.getId()).isEqualTo(saved.getId());
            assertThat(foundModel.getIdentifier()).isEqualTo(identifier);
            assertThat(foundModel.getNickname().getValue()).isEqualTo(nickname);
            assertThat(foundModel.getPhoneNumber().getValue()).isEqualTo(phoneNumber);
            assertThat(foundModel.getBirthday()).isEqualTo(birthday);
            assertThat(foundModel.getPassword().getValue()).isNotEqualTo(rawPassword); // 암호화됨
            assertThat(foundModel.getCreatedAt()).isNotNull();
            assertThat(foundModel.getUpdatedAt()).isNotNull();
        }
    }
}