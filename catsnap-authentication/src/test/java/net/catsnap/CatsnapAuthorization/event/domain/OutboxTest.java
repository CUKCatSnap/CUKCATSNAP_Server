package net.catsnap.CatsnapAuthorization.event.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Outbox 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OutboxTest {

    @Nested
    class prepareForPublishing_테스트 {

        @Test
        void correlationId가_null이면_eventId를_correlationId로_사용한다() {
            // given
            String aggregateType = "Photographer";
            String aggregateId = "12345";
            String eventType = "PhotographerCreated";
            byte[] payload = new byte[]{1, 2, 3};
            int version = 1;
            Instant timestamp = Instant.now();

            // when
            Outbox outbox = Outbox.prepareForPublishing(
                aggregateType, aggregateId, eventType, payload, version, timestamp,
                null, null
            );

            // then
            assertThat(outbox.getEventId()).isNotNull();
            assertThat(outbox.getCorrelationId()).isEqualTo(outbox.getEventId());
        }

        @Test
        void correlationId가_제공되면_그대로_사용한다() {
            // given
            String correlationId = "custom-correlation-id";

            // when
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), correlationId, null
            );

            // then
            assertThat(outbox.getCorrelationId()).isEqualTo(correlationId);
            assertThat(outbox.getEventId()).isNotEqualTo(correlationId);
        }

        @Test
        void 초기_상태는_PENDING이다() {
            // when
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // then
            assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.PENDING);
        }

        @Test
        void 초기_retryCount는_0이다() {
            // when
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // then
            assertThat(outbox.getRetryCount()).isEqualTo(0);
        }

        @Test
        void 모든_필드가_올바르게_설정된다() {
            // given
            String aggregateType = "Photographer";
            String aggregateId = "12345";
            String eventType = "PhotographerCreated";
            byte[] payload = new byte[]{1, 2, 3};
            int version = 1;
            Instant timestamp = Instant.now();
            String causationId = "causation-123";

            // when
            Outbox outbox = Outbox.prepareForPublishing(
                aggregateType, aggregateId, eventType, payload, version, timestamp,
                null, causationId
            );

            // then
            assertThat(outbox.getAggregateType()).isEqualTo(aggregateType);
            assertThat(outbox.getAggregateId()).isEqualTo(aggregateId);
            assertThat(outbox.getEventType()).isEqualTo(eventType);
            assertThat(outbox.getPayload()).isEqualTo(payload);
            assertThat(outbox.getVersion()).isEqualTo(version);
            assertThat(outbox.getTimestamp()).isEqualTo(timestamp);
            assertThat(outbox.getCausationId()).isEqualTo(causationId);
        }
    }

    @Nested
    class markAsPublished_테스트 {

        @Test
        void status가_PUBLISHED로_변경된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // when
            outbox.markAsPublished();

            // then
            assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.PUBLISHED);
        }

        @Test
        void publishedAt이_설정된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // when
            outbox.markAsPublished();

            // then
            assertThat(outbox.getPublishedAt()).isNotNull();
        }
    }

    @Nested
    class markAsFailed_테스트 {

        @Test
        void status가_FAILED로_변경된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // when
            outbox.markAsFailed("에러 발생");

            // then
            assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.FAILED);
        }

        @Test
        void retryCount가_증가한다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            int initialRetryCount = outbox.getRetryCount();

            // when
            outbox.markAsFailed("에러 발생");

            // then
            assertThat(outbox.getRetryCount()).isEqualTo(initialRetryCount + 1);
        }

        @Test
        void lastError가_설정된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            String errorMessage = "Kafka 연결 실패";

            // when
            outbox.markAsFailed(errorMessage);

            // then
            assertThat(outbox.getLastError()).isEqualTo(errorMessage);
        }

        @Test
        void 에러_메시지가_1000자를_초과하면_잘린다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            String longErrorMessage = "A".repeat(1500);

            // when
            outbox.markAsFailed(longErrorMessage);

            // then
            assertThat(outbox.getLastError()).hasSize(1000);
            assertThat(outbox.getLastError()).isEqualTo(longErrorMessage.substring(0, 1000));
        }

        @Test
        void null_에러_메시지도_처리된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // when
            outbox.markAsFailed(null);

            // then
            assertThat(outbox.getLastError()).isNull();
        }
    }

    @Nested
    class canRetry_테스트 {

        @Test
        void PENDING_상태이고_retryCount가_maxRetryCount_미만이면_true를_반환한다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            int maxRetryCount = 3;

            // when & then
            assertThat(outbox.canRetry(maxRetryCount)).isTrue();
        }

        @Test
        void FAILED_상태이면_false를_반환한다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            outbox.markAsFailed("에러");
            int maxRetryCount = 3;

            // when & then
            assertThat(outbox.canRetry(maxRetryCount)).isFalse();
        }

        @Test
        void retryCount가_maxRetryCount_이상이면_false를_반환한다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            int maxRetryCount = 3;

            // 재시도를 3번 수행
            outbox.retry();
            outbox.retry();
            outbox.retry();

            // when & then
            assertThat(outbox.getRetryCount()).isEqualTo(3);
            assertThat(outbox.canRetry(maxRetryCount)).isFalse();
        }
    }

    @Nested
    class retry_테스트 {

        @Test
        void status가_PENDING으로_변경된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );

            // when
            outbox.retry();

            // then
            assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.PENDING);
        }

        @Test
        void retryCount가_증가한다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            int initialRetryCount = outbox.getRetryCount();

            // when
            outbox.retry();

            // then
            assertThat(outbox.getRetryCount()).isEqualTo(initialRetryCount + 1);
        }

        @Test
        void FAILED_상태에서도_PENDING으로_변경된다() {
            // given
            Outbox outbox = Outbox.prepareForPublishing(
                "Photographer", "12345", "PhotographerCreated", new byte[]{1, 2, 3}, 1,
                Instant.now(), null, null
            );
            outbox.markAsFailed("에러");

            // when
            outbox.retry();

            // then
            assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.PENDING);
        }
    }
}