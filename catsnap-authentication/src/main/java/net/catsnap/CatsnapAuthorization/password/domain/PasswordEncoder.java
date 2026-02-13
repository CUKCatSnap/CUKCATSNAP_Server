package net.catsnap.CatsnapAuthorization.password.domain;

/**
 * 비밀번호 인코딩 및 매칭을 위한 인터페이스. 이 인터페이스를 구현하여 다양한 비밀번호 해싱 알고리즘을 사용할 수 있습니다.
 */
public interface PasswordEncoder {

    /**
     * 원본 비밀번호를 인코딩합니다.
     *
     * @param rawPassword 인코딩할 원본 비밀번호
     * @return 인코딩된 비밀번호
     */
    String encode(String rawPassword);

    /**
     * 원본 비밀번호와 인코딩된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword     사용자가 입력한 원본 비밀번호
     * @param encodedPassword 저장된 인코딩된 비밀번호
     * @return 두 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    boolean matches(String rawPassword, String encodedPassword);
}