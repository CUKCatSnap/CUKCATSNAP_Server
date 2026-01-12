package net.catsnap.CatsnapAuthorization.event.domain;

/**
 * 아웃박스 이벤트 상태
 */
public enum OutboxStatus {
    /**
     * 발행 대기 중
     */
    PENDING,

    /**
     * Kafka로 발행 완료
     */
    PUBLISHED,

    /**
     * 발행 실패 (재시도 횟수 초과)
     */
    FAILED
}