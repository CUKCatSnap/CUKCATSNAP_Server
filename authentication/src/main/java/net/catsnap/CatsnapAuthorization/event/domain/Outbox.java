package net.catsnap.CatsnapAuthorization.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 아웃박스 패턴을 위한 이벤트 임시 저장소
 *
 * <p>도메인 이벤트를 발행하기 전에 데이터베이스에 저장하여
 * 트랜잭션 일관성과 최소 1회 전달(At-Least-Once Delivery)을 보장합니다.</p>
 *
 * <p>이벤트는 먼저 도메인 트랜잭션 내에서 Outbox 테이블에 저장되고,
 * 별도의 릴레이 프로세스가 주기적으로 폴링합니다.</p>
 *
 * <p>EventEnvelope의 모든 필드를 저장하여 이벤트 발생 시점의 정보를 정확히 보존하고,
 * 재시도 시에도 동일한 이벤트를 발행할 수 있도록 합니다 (멱등성 보장).</p>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Outbox {

    private static final int MAX_ERROR_MESSAGE_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String eventType;

    @Lob
    @Column(nullable = false, columnDefinition = "BYTEA")
    private byte[] payload;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private Instant timestamp;

    private String correlationId;

    private String causationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private int retryCount;

    @Column(length = MAX_ERROR_MESSAGE_LENGTH)
    private String lastError;

    /**
     * 이벤트 발행을 위해 아웃박스에 준비합니다.
     *
     * <p>도메인 이벤트를 Kafka로 발행하기 전 Outbox 테이블에 저장하기 위한 엔티티를 생성합니다.
     * eventId는 UUID로 자동 생성되며, correlationId가 null인 경우(최초 이벤트)
     * 자동으로 eventId를 correlationId로 설정하여 이후 연관된 이벤트들이
     * 동일한 correlationId로 추적될 수 있도록 합니다.</p>
     *
     * @param aggregateType 애그리거트 타입 (예: Photographer)
     * @param aggregateId   애그리거트 ID
     * @param eventType     이벤트 타입 (예: PhotographerCreated)
     * @param payload       직렬화된 이벤트 페이로드 (바이너리)
     * @param version       스키마 버전
     * @param timestamp     이벤트 발생 시점
     * @param correlationId 상관관계 ID (null이면 eventId 사용)
     * @param causationId   인과관계 ID (이벤트 체인 추적용)
     * @return 발행 준비된 Outbox 엔티티
     */
    public static Outbox prepareForPublishing(String aggregateType, String aggregateId,
        String eventType, byte[] payload, int version, Instant timestamp, String correlationId,
        String causationId) {
        String eventId = java.util.UUID.randomUUID().toString();

        // 최초 이벤트인 경우 자기 자신의 eventId를 correlationId로 설정
        String finalCorrelationId = correlationId != null ? correlationId : eventId;

        return new Outbox(eventId, aggregateType, aggregateId, eventType, payload, version,
            timestamp, finalCorrelationId, causationId);
    }

    private Outbox(String eventId, String aggregateType, String aggregateId, String eventType,
        byte[] payload, int version, Instant timestamp, String correlationId, String causationId) {
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.version = version;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.status = OutboxStatus.PENDING;
        this.retryCount = 0;
    }

    /**
     * 이벤트 발행 성공 처리
     *
     * <p>Kafka로 이벤트가 성공적으로 발행되었을 때 호출됩니다.</p>
     */
    public void markAsPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 이벤트 발행 실패 처리
     *
     * <p>이벤트 발행 실패 시 재시도 횟수를 증가시키고 에러 메시지를 기록합니다.</p>
     *
     * @param errorMessage 실패 사유
     */
    public void markAsFailed(String errorMessage) {
        this.status = OutboxStatus.FAILED;
        this.retryCount++;
        this.lastError = truncateErrorMessage(errorMessage);
    }

    /**
     * 재시도 가능 여부 확인
     *
     * @param maxRetryCount 최대 재시도 횟수
     * @return 재시도 가능하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean canRetry(int maxRetryCount) {
        return this.status == OutboxStatus.PENDING && this.retryCount < maxRetryCount;
    }

    /**
     * 재시도 처리
     *
     * <p>이벤트를 다시 발행 대기 상태로 변경하고 재시도 횟수를 증가시킵니다.</p>
     */
    public void retry() {
        this.status = OutboxStatus.PENDING;
        this.retryCount++;
    }

    private String truncateErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            return null;
        }
        return errorMessage.length() > MAX_ERROR_MESSAGE_LENGTH
            ? errorMessage.substring(0, MAX_ERROR_MESSAGE_LENGTH)
            : errorMessage;
    }
}