package net.catsnap.CatsnapAuthorization.event.infrastructure;

import static net.catsnap.CatsnapAuthorization.event.domain.OutboxFixture.aOutbox;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.domain.OutboxStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("OutboxRepository 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OutboxRepositoryTest {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void PENDING_상태의_이벤트만_조회된다() {
        // given
        Outbox pending1 = aOutbox().withAggregateId("1").build();
        Outbox pending2 = aOutbox().withAggregateId("2").build();
        Outbox published = aOutbox().withAggregateId("3").build();
        published.markAsPublished();

        entityManager.persist(pending1);
        entityManager.persist(pending2);
        entityManager.persist(published);
        entityManager.flush();

        // when
        List<Outbox> result = outboxRepository.findByStatus(OutboxStatus.PENDING);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(Outbox::getAggregateId)
            .containsExactlyInAnyOrder("1", "2");
    }

    @Test
    void PUBLISHED_상태의_이벤트만_조회된다() {
        // given
        Outbox pending = aOutbox().withAggregateId("1").build();
        Outbox published1 = aOutbox().withAggregateId("2").build();
        Outbox published2 = aOutbox().withAggregateId("3").build();
        published1.markAsPublished();
        published2.markAsPublished();

        entityManager.persist(pending);
        entityManager.persist(published1);
        entityManager.persist(published2);
        entityManager.flush();

        // when
        List<Outbox> result = outboxRepository.findByStatus(OutboxStatus.PUBLISHED);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(Outbox::getAggregateId)
            .containsExactlyInAnyOrder("2", "3");
    }

    @Test
    void FAILED_상태의_이벤트만_조회된다() {
        // given
        Outbox pending = aOutbox().withAggregateId("1").build();
        Outbox failed1 = aOutbox().withAggregateId("2").build();
        Outbox failed2 = aOutbox().withAggregateId("3").build();
        failed1.markAsFailed("에러1");
        failed2.markAsFailed("에러2");

        entityManager.persist(pending);
        entityManager.persist(failed1);
        entityManager.persist(failed2);
        entityManager.flush();

        // when
        List<Outbox> result = outboxRepository.findByStatus(OutboxStatus.FAILED);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(Outbox::getAggregateId)
            .containsExactlyInAnyOrder("2", "3");
    }

    @Test
    void 해당_상태의_이벤트가_없으면_빈_리스트를_반환한다() {
        // given
        Outbox pending = aOutbox().build();
        entityManager.persist(pending);
        entityManager.flush();

        // when
        List<Outbox> result = outboxRepository.findByStatus(OutboxStatus.PUBLISHED);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 여러_애그리거트_타입의_이벤트도_상태별로_조회된다() {
        // given
        Outbox photographerPending = aOutbox()
            .withAggregateType("Photographer")
            .withAggregateId("1")
            .build();
        Outbox reservationPending = aOutbox()
            .withAggregateType("Reservation")
            .withAggregateId("2")
            .build();
        Outbox photographerPublished = aOutbox()
            .withAggregateType("Photographer")
            .withAggregateId("3")
            .build();
        photographerPublished.markAsPublished();

        entityManager.persist(photographerPending);
        entityManager.persist(reservationPending);
        entityManager.persist(photographerPublished);
        entityManager.flush();

        // when
        List<Outbox> pendingResult = outboxRepository.findByStatus(OutboxStatus.PENDING);
        List<Outbox> publishedResult = outboxRepository.findByStatus(OutboxStatus.PUBLISHED);

        // then
        assertThat(pendingResult).hasSize(2);
        assertThat(pendingResult)
            .extracting(Outbox::getAggregateType)
            .containsExactlyInAnyOrder("Photographer", "Reservation");

        assertThat(publishedResult).hasSize(1);
        assertThat(publishedResult.get(0).getAggregateType()).isEqualTo("Photographer");
    }
}