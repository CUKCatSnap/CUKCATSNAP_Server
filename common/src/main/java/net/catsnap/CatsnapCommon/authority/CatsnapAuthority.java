package net.catsnap.CatsnapCommon.authority;

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
        return super.toString().toLowerCase();
    }
}
