package net.catsnap.CatsnapAuthorization.photographer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Photographer 도메인 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Test
    void 모든_정보를_포함하여_회원가입에_성공한다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nameValue = "홍길동";
        String phoneNumberValue = "010-1234-5678";

        String encodedPasswordValue = "$2a$10$encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);

        // when
        Photographer photographer = Photographer.signUp(identifierValue, rawPasswordValue,
            nameValue, phoneNumberValue, passwordEncoder);

        // then
        assertThat(photographer.getIdentifier().getValue()).isEqualTo(identifierValue);
        assertThat(photographer.getPassword().getValue()).isEqualTo(encodedPasswordValue);
        assertThat(photographer.getName().getValue()).isEqualTo(nameValue);
        assertThat(photographer.getPhoneNumber().getValue()).isEqualTo(phoneNumberValue);
        assertThat(photographer.getProfilePhotoUrl()).isEqualTo("https://placehold.co/100x100");
        verify(passwordEncoder).encode(rawPasswordValue);
    }

    @Test
    void 회원가입_시_비밀번호가_암호화된다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nameValue = "홍길동";
        String phoneNumberValue = "010-1234-5678";

        String encodedPasswordValue = "$2a$10$encodedPassword";
        when(passwordEncoder.encode(rawPasswordValue)).thenReturn(encodedPasswordValue);

        // when
        Photographer photographer = Photographer.signUp(identifierValue, rawPasswordValue,
            nameValue, phoneNumberValue, passwordEncoder);

        // then
        assertThat(photographer.getPassword().getValue()).isEqualTo(encodedPasswordValue);
        assertThat(photographer.getPassword().getValue()).isNotEqualTo(rawPasswordValue);
        verify(passwordEncoder).encode(rawPasswordValue);
    }

    @Test
    void 회원가입_시_기본_프로필_사진_URL이_설정된다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nameValue = "홍길동";
        String phoneNumberValue = "010-1234-5678";

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");

        // when
        Photographer photographer = Photographer.signUp(identifierValue, rawPasswordValue,
            nameValue, phoneNumberValue, passwordEncoder);

        // then
        assertThat(photographer.getProfilePhotoUrl()).isNotNull();
        assertThat(photographer.getProfilePhotoUrl()).isEqualTo("https://placehold.co/100x100");
    }

    @Test
    void 올바른_비밀번호로_로그인하면_true를_반환한다() {
        // given
        String rawPasswordValue = "password1234";
        String encodedPasswordValue = "$2a$10$encodedPassword";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);
        when(passwordEncoder.matches(eq(rawPasswordValue), eq(encodedPasswordValue))).thenReturn(
            true);

        Photographer photographer = Photographer.signUp("testuser", rawPasswordValue, "홍길동",
            "010-1234-5678", passwordEncoder);

        // when
        boolean result = photographer.validatePassword(rawPasswordValue, passwordEncoder);

        // then
        assertThat(result).isTrue();
        verify(passwordEncoder).matches(eq(rawPasswordValue), eq(encodedPasswordValue));
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_false를_반환한다() {
        // given
        String correctPassword = "password1234";
        String wrongPassword = "wrongpassword";
        String encodedPasswordValue = "$2a$10$encodedPassword";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);
        when(passwordEncoder.matches(eq(wrongPassword), eq(encodedPasswordValue))).thenReturn(
            false);

        Photographer photographer = Photographer.signUp("testuser", correctPassword, "홍길동",
            "010-1234-5678", passwordEncoder);

        // when
        boolean result = photographer.validatePassword(wrongPassword, passwordEncoder);

        // then
        assertThat(result).isFalse();
        verify(passwordEncoder).matches(eq(wrongPassword), eq(encodedPasswordValue));
    }
}