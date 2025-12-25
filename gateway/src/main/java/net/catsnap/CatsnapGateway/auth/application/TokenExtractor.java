package net.catsnap.CatsnapGateway.auth.application;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * HTTP 요청에서 토큰을 추출하는 인터페이스입니다.
 * 이 인터페이스는 Application 레이어에서 Infrastructure 레이어를 추상화하기 위한 포트(Port)입니다.
 */
public interface TokenExtractor {

    /**
     * HTTP 요청에서 토큰을 추출합니다.
     * 토큰이 없거나 형식이 잘못된 경우 빈 Optional을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 토큰을 담은 Optional. 토큰이 없으면 Optional.empty()
     */
    Optional<Token> extract(ServerHttpRequest request);
}
