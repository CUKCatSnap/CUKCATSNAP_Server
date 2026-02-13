package net.catsnap.CatsnapAuthorization.password.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * BCryptPasswordEncoder 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BCryptPasswordEncoderTest {

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder delegate;

    @InjectMocks
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void 비밀번호를_인코딩하면_delegate의_encode를_호출한다() {
        // given
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encoded.hash.value";
        when(delegate.encode(rawPassword)).thenReturn(encodedPassword);

        // when
        String result = passwordEncoder.encode(rawPassword);

        // then
        assertThat(result).isEqualTo(encodedPassword);
        verify(delegate).encode(rawPassword);
    }

    @Test
    void 올바른_비밀번호를_검증하면_true를_반환한다() {
        // given
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encoded.hash.value";
        when(delegate.matches(rawPassword, encodedPassword)).thenReturn(true);

        // when
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertThat(result).isTrue();
        verify(delegate).matches(rawPassword, encodedPassword);
    }

    @Test
    void 잘못된_비밀번호를_검증하면_false를_반환한다() {
        // given
        String rawPassword = "password123";
        String wrongPassword = "wrongPassword";
        String encodedPassword = "$2a$10$encoded.hash.value";
        when(delegate.matches(wrongPassword, encodedPassword)).thenReturn(false);

        // when
        boolean result = passwordEncoder.matches(wrongPassword, encodedPassword);

        // then
        assertThat(result).isFalse();
        verify(delegate).matches(wrongPassword, encodedPassword);
    }
}
