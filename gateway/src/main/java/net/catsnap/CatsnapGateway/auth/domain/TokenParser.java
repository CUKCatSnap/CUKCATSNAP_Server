package net.catsnap.CatsnapGateway.auth.domain;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;

/**
 * 토큰을 파싱하여 클레임 정보를 추출하는 인터페이스입니다.
 * 이 인터페이스는 도메인 레이어에서 인프라 레이어를 추상화하기 위한 포트(Port)입니다.
 */
public interface TokenParser {

    /**
     * 토큰을 파싱하여 클레임 정보를 추출합니다.
     * 토큰이 유효하지 않거나 파싱에 실패하면 빈 Optional을 반환합니다.
     *
     * @param token 파싱할 토큰
     * @return 파싱된 클레임 정보를 담은 Optional. 파싱 실패 시 Optional.empty()
     */
    Optional<TokenClaims> parse(Token token);
}
