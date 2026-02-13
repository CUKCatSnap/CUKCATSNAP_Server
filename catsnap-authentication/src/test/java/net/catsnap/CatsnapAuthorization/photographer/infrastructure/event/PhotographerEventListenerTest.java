package net.catsnap.CatsnapAuthorization.photographer.infrastructure.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import net.catsnap.CatsnapAuthorization.event.application.EventPublisher;
import net.catsnap.CatsnapAuthorization.photographer.domain.events.PhotographerCreatedEvent;
import net.catsnap.event.photographer.v1.PhotographerCreated;
import net.catsnap.shared.application.EventSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PhotographerEventListener 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerEventListenerTest {

    private EventPublisher eventPublisher;
    private EventSerializer eventSerializer;
    private PhotographerEventListener listener;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(EventPublisher.class);
        eventSerializer = mock(EventSerializer.class);
        listener = new PhotographerEventListener(eventPublisher, eventSerializer);
    }

    @Test
    void 작가_생성_이벤트_처리시_EventPublisher가_올바른_파라미터로_호출된다() {
        // given
        Long photographerId = 12345L;
        Instant timestamp = Instant.parse("2026-01-11T10:00:00Z");
        PhotographerCreatedEvent event = new PhotographerCreatedEvent(photographerId, timestamp);

        byte[] serializedPayload = new byte[]{1, 2, 3, 4, 5};
        when(eventSerializer.serialize(any())).thenReturn(serializedPayload);

        // when
        listener.handlePhotographerCreatedEvent(event);

        // then
        verify(eventPublisher).publish(
            eq("Photographer"),               // aggregateType
            eq(photographerId.toString()),    // aggregateId
            eq("PhotographerCreated"),        // eventType
            eq(serializedPayload),            // eventPayload
            eq(1),                            // version
            eq(timestamp),                    // timestamp
            eq(null),                         // correlationId
            eq(null)                          // causationId
        );
    }
}