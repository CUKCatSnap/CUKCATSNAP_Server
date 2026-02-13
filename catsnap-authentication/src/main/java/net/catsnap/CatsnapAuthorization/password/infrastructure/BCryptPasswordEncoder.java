package net.catsnap.CatsnapAuthorization.password.infrastructure;

import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt 해싱 알고리즘을 사용하는 PasswordEncoder 구현체.
 * Spring Security의 PasswordEncoder를 위임받아 비밀번호 인코딩 및 검증을 수행합니다.
 */
@Component
public class BCryptPasswordEncoder implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder delegate;

    /**
     * BCryptPasswordEncoder 생성자.
     *
     * @param delegate Spring Security의 PasswordEncoder 구현체 (BCrypt)
     */
    public BCryptPasswordEncoder(
        org.springframework.security.crypto.password.PasswordEncoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}