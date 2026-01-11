package net.catsnap.CatsnapAuthorization.event.domain;

import java.time.Instant;

/**
 * Outbox 도메인 테스트를 위한 Fixture 클래스
 *
 * <p>테스트에서 Outbox 엔티티를 쉽게 생성하고 관리할 수 있도록 돕는 빌더 패턴 제공</p>
 */
public class OutboxFixture {

    private String aggregateType = "Photographer";
    private String aggregateId = "12345";
    private String eventType = "PhotographerCreated";
    private byte[] payload = new byte[]{1, 2, 3, 4, 5};
    private int version = 1;
    private Instant timestamp = Instant.parse("2026-01-11T10:00:00Z");
    private String correlationId = null;
    private String causationId = null;

    public static OutboxFixture aOutbox() {
        return new OutboxFixture();
    }

    public OutboxFixture withAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
        return this;
    }

    public OutboxFixture withAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public OutboxFixture withEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public OutboxFixture withPayload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public OutboxFixture withVersion(int version) {
        this.version = version;
        return this;
    }

    public OutboxFixture withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public OutboxFixture withCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public OutboxFixture withCausationId(String causationId) {
        this.causationId = causationId;
        return this;
    }

    public Outbox build() {
        return Outbox.prepareForPublishing(
            aggregateType,
            aggregateId,
            eventType,
            payload,
            version,
            timestamp,
            correlationId,
            causationId
        );
    }

    /**
     * 빠른 생성을 위한 정적 팩토리 메서드
     */
    public static Outbox createDefault() {
        return aOutbox().build();
    }

    /**
     * 특정 상태의 Outbox 생성
     */
    public static Outbox createWithStatus(OutboxStatus status) {
        Outbox outbox = createDefault();
        if (status == OutboxStatus.PUBLISHED) {
            outbox.markAsPublished();
        } else if (status == OutboxStatus.FAILED) {
            outbox.markAsFailed("테스트 실패");
        }
        return outbox;
    }
}