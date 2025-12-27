package net.catsnap.CatsnapAuthorization.photographer.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;
import net.catsnap.CatsnapAuthorization.photographer.fixture.PhotographerTestFixture;
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
class PhotographerRepositoryTest {

    @Autowired
    private PhotographerRepository photographerRepository;

    @Nested
    class existsByIdentifier_메서드_단위_테스트 {

        @Test
        void 존재하는_Identifier는_true를_반환한다() {
            // given
            Identifier identifier = new Identifier("testphotographer");
            Photographer photographer = PhotographerTestFixture.builder()
                .identifier(identifier.getValue())
                .build();
            photographerRepository.save(photographer);

            // when
            boolean exists = photographerRepository.existsByIdentifier(identifier);

            // then
            assertTrue(exists);
        }

        @Test
        void 존재하지_않는_Identifier는_false를_반환한다() {
            // given
            Identifier nonExistentIdentifier = new Identifier("nonexistent");

            // when
            boolean exists = photographerRepository.existsByIdentifier(nonExistentIdentifier);

            // then
            assertFalse(exists);
        }
    }

    @Nested
    class findByIdentifier_메서드_단위_테스트 {

        @Test
        void 존재하는_Identifier로_조회하면_Photographer를_반환한다() {
            // given
            Identifier identifier = new Identifier("findtest");
            Photographer savedPhotographer = PhotographerTestFixture.builder()
                .identifier(identifier.getValue())
                .name("테스트작가")
                .build();
            photographerRepository.save(savedPhotographer);

            // when
            Optional<Photographer> result = photographerRepository.findByIdentifier(identifier);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getIdentifier()).isEqualTo(identifier);
            assertThat(result.get().getName().getValue()).isEqualTo("테스트작가");
        }

        @Test
        void findByIdentifier_존재하지_않는_Identifier로_조회하면_빈_Optional을_반환한다() {
            // given
            Identifier nonExistentIdentifier = new Identifier("nonexistent");

            // when
            Optional<Photographer> result = photographerRepository.findByIdentifier(
                nonExistentIdentifier);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void findByIdentifier_조회된_Photographer의_모든_필드가_저장된_값과_일치한다() {
            // given
            Identifier identifier = new Identifier("fulltest");
            String name = "풀테스트작가";
            String phoneNumber = "010-9999-8888";
            String rawPassword = "testpass1234";

            Photographer savedPhotographer = PhotographerTestFixture.builder()
                .identifier(identifier.getValue())
                .name(name)
                .phoneNumber(phoneNumber)
                .password(rawPassword)
                .build();
            Photographer saved = photographerRepository.save(savedPhotographer);

            // when
            Optional<Photographer> result = photographerRepository.findByIdentifier(identifier);

            // then
            assertThat(result).isPresent();
            Photographer foundPhotographer = result.get();
            assertThat(foundPhotographer.getId()).isEqualTo(saved.getId());
            assertThat(foundPhotographer.getIdentifier()).isEqualTo(identifier);
            assertThat(foundPhotographer.getName().getValue()).isEqualTo(name);
            assertThat(foundPhotographer.getPhoneNumber().getValue()).isEqualTo(phoneNumber);
            assertThat(foundPhotographer.getPassword().getValue()).isNotEqualTo(rawPassword); // 암호화됨
            assertThat(foundPhotographer.getCreatedAt()).isNotNull();
            assertThat(foundPhotographer.getUpdatedAt()).isNotNull();
        }
    }
}