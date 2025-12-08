package net.catsnap.CatsnapGateway.auth.service;

import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * `TokenService` 인터페이스는 HTTP 요청에서 인증 토큰을 처리하고 사용자 인증 정보를 추출하는 기능을 정의합니다. 이 인터페이스를 구현하는 클래스는 토큰의
 * 유효성을 검증하고, 토큰 내부에 포함된 사용자 정보를 파싱하는 역할을 수행합니다.
 */
public interface TokenService {

    /**
     * 주어진 `ServerHttpRequest`에서 인증 토큰을 추출하고 파싱하여 사용자 인증 정보를 반환합니다. 토큰이 유효하고 사용자 정보를 포함하고 있는 경우,
     * `UserAuthInformation` 객체를 반환합니다. 토큰이 없거나 유효하지 않은 경우에는 익명 사용자 정보를 반환합니다.
     *
     * @param serverHttpRequest 사용자 인증 정보를 추출할 `ServerHttpRequest` 객체.
     * @return 사용자 인증 정보를 담고 있는 `UserAuthInformation` 객체. 토큰이 유효하지 않거나 없는 경우 `null`을 반환합니다.
     */
    UserAuthInformation getUserAuthInformation(ServerHttpRequest serverHttpRequest);
}
