package net.catsnap.CatsnapGateway.auth.dto;

import net.catsnap.shared.auth.CatsnapAuthority;

/**
 * 사용자 인증 정보를 담는 레코드 클래스입니다. 이 클래스는 인증된 사용자의 고유 ID와 권한 정보를 불변 객체로 저장합니다.
 */
public record AuthenticationPassport(
    /**
     * 사용자의 고유 ID
     */
    Long userId,
    /**
     * 사용자의 권한 (예: "PHOTOGRAPHER", "MODEL", "ANONYMOUS", "ADMIN" )
     */
    CatsnapAuthority authority
) {

}
