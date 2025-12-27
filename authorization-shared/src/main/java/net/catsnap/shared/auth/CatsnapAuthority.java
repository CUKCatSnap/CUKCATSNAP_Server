package net.catsnap.shared.auth;

import java.util.Optional;

/**
 * Catsnap 서비스에서 사용되는 사용자 권한을 정의하는 ENUM 클래스 입니다. 각 권한은 시스템 내에서 특정 역할과 접근 수준을 나타냅니다.
 */
public enum CatsnapAuthority {
    /**
     * 권한 정의
     */
    MODEL, PHOTOGRAPHER, ANONYMOUS, ADMIN;

    /**
     * 현재 권한의 이름을 소문자 문자열로 반환합니다.
     *
     * @return 권한의 소문자 문자열 표현
     */
    public String getAuthorityName() {
        return name().toLowerCase();
    }

    /**
     * 주어진 권한 이름(문자열)에 해당하는 CatsnapAuthority 상수를 반환합니다. 대소문자를 구분하지 않고, 주어진 문자열과 일치하는
     * CatsnapAuthority 열거형 상수를 찾아 반환합니다.
     *
     * @param authorityName 권한의 이름 (예: "model", "admin")
     * @return 일치하는 CatsnapAuthority 상수를 포함하는 Optional 객체, 일치하는 상수가 없으면 빈 Optional 객체
     */
    public static Optional<CatsnapAuthority> findAuthorityByName(String authorityName) {
        if (authorityName == null) {
            return Optional.empty();
        }
        for (CatsnapAuthority authority : CatsnapAuthority.values()) {
            if (authority.name().equalsIgnoreCase(authorityName)) {
                return Optional.of(authority);
            }
        }
        return Optional.empty();
    }

    /**
     * 권한을 바이트 값으로 변환합니다. Passport 직렬화에 사용됩니다.
     *
     * @return 권한의 바이트 표현 (MODEL=0, PHOTOGRAPHER=1, ADMIN=2, ANONYMOUS=3)
     */
    public byte toByte() {
        return switch (this) {
            case MODEL -> 0;
            case PHOTOGRAPHER -> 1;
            case ADMIN -> 2;
            case ANONYMOUS -> 3;
        };
    }

    /**
     * 바이트 값에서 권한을 생성합니다. Passport 역직렬화에 사용됩니다.
     *
     * @param value 권한의 바이트 값
     * @return 해당하는 CatsnapAuthority 상수
     * @throws IllegalArgumentException 유효하지 않은 바이트 값인 경우
     */
    public static CatsnapAuthority fromByte(byte value) {
        return switch (value) {
            case 0 -> MODEL;
            case 1 -> PHOTOGRAPHER;
            case 2 -> ADMIN;
            case 3 -> ANONYMOUS;
            default -> throw new IllegalArgumentException("유효하지 않은 권한 바이트 값: " + value);
        };
    }
}
