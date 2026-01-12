package net.catsnap.CatsnapAuthorization.photographer.domain.events;

import java.time.Instant;

/**
 * Photographer 생성 도메인 이벤트
 *
 * <p>작가가 시스템에 회원가입했을 때 발행되는 도메인 이벤트입니다. </p>
 */
public record PhotographerCreatedEvent(
    Long photographerId,
    Instant timestamp
) {
}