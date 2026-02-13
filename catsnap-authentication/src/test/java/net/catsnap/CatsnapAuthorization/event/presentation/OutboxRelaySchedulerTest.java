package net.catsnap.CatsnapAuthorization.event.presentation;

import static org.mockito.Mockito.verify;

import net.catsnap.CatsnapAuthorization.event.application.OutboxRelayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OutboxRelayScheduler 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OutboxRelaySchedulerTest {

    @Mock
    private OutboxRelayService outboxRelayService;

    @InjectMocks
    private OutboxRelayScheduler scheduler;

    @Test
    void relayOutboxEvents_호출_시_OutboxRelayService를_호출한다() {
        // when
        scheduler.relayOutboxEvents();

        // then
        verify(outboxRelayService).relayPendingEvents();
    }
}
