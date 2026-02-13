package net.catsnap.CatsnapAuthorization.event.application;

import java.util.function.Consumer;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;

/**
 * 이벤트 릴레이 인터페이스
 *
 * <p>Outbox 패턴에서 저장된 이벤트를 외부 메시징 시스템으로 전달합니다.
 * DDD의 infrastructure 관심사를 application 계층에서 추상화합니다.</p>
 */
public interface EventRelay {

    /**
     * 이벤트를 외부 메시징 시스템으로 비동기 전송합니다.
     *
     * @param outbox 발행할 이벤트 정보
     * @param onSuccess 발행 성공 시 실행할 콜백
     * @param onFailure 발행 실패 시 실행할 콜백 (예외를 인자로 받음)
     */
    void relay(Outbox outbox, Runnable onSuccess, Consumer<Throwable> onFailure);
}
