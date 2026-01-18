package net.catsnap.CatsnapAuthorization.event.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.domain.OutboxStatus;
import net.catsnap.CatsnapAuthorization.event.infrastructure.OutboxRepository;
import net.catsnap.event.shared.EventEnvelope;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = {"PhotographerCreated"},
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@DisplayName("OutboxRelayService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpringJavaInjectionPointsAutowiringInspection"})
class OutboxRelayServiceTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private OutboxRelayService outboxRelayService;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ConsumerFactory<String, EventEnvelope> consumerFactory;

    private KafkaMessageListenerContainer<String, EventEnvelope> container;
    private BlockingQueue<ConsumerRecord<String, EventEnvelope>> records;

    @BeforeEach
    void setUp() {
        records = new LinkedBlockingQueue<>();

        ContainerProperties containerProperties = new ContainerProperties("PhotographerCreated");
        containerProperties.setGroupId("test-group");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, EventEnvelope>) records::add);
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterEach
    void tearDown() {
        if (container != null) {
            container.stop();
        }
        transactionTemplate.executeWithoutResult(status -> outboxRepository.deleteAll());
    }

    @Test
    void PENDING_상태의_Outbox_이벤트가_Kafka로_발행되고_PUBLISHED_상태로_변경된다() throws InterruptedException {
        // given
        Outbox outbox = Outbox.prepareForPublishing(
            "Photographer",
            "photographer-123",
            "PhotographerCreated",
            new byte[]{1, 2, 3},
            1,
            Instant.now(),
            null,
            null
        );

        transactionTemplate.executeWithoutResult(status -> outboxRepository.save(outbox));

        // when
        outboxRelayService.relayPendingEvents();

        // then - Kafka 메시지 수신 확인
        ConsumerRecord<String, EventEnvelope> received = records.poll(10, TimeUnit.SECONDS);
        assertThat(received).isNotNull();
        assertThat(received.key()).isEqualTo("photographer-123");
        assertThat(received.value().getAggregateType()).isEqualTo("Photographer");
        assertThat(received.value().getEventType()).isEqualTo("PhotographerCreated");

        // then - Outbox 상태 확인 (비동기 콜백이므로 약간의 대기 필요)
        Thread.sleep(500);
        transactionTemplate.executeWithoutResult(status -> {
            Outbox updated = outboxRepository.findAll().get(0);
            assertThat(updated.getStatus()).isEqualTo(OutboxStatus.PUBLISHED);
        });
    }

    @Test
    void 발행할_이벤트가_없으면_아무_동작도_하지_않는다() throws InterruptedException {
        // given - Outbox가 비어있음

        // when
        outboxRelayService.relayPendingEvents();

        // then
        ConsumerRecord<String, EventEnvelope> received = records.poll(2, TimeUnit.SECONDS);
        assertThat(received).isNull();
    }
}
