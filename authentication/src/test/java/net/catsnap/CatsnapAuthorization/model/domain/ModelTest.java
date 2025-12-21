package net.catsnap.CatsnapAuthorization.model.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Model 도메인 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Test
    void 모든_정보를_포함하여_회원가입에_성공한다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nicknameValue = "테스터";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String phoneNumberValue = "010-1234-5678";

        String encodedPasswordValue = "$2a$10$encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);

        // when
        Model model = Model.signUp(identifierValue, rawPasswordValue, nicknameValue, birthday,
            phoneNumberValue, passwordEncoder);

        // then
        assertThat(model.getIdentifier().getValue()).isEqualTo(identifierValue);
        assertThat(model.getPassword().getValue()).isEqualTo(encodedPasswordValue);
        assertThat(model.getNickname().getValue()).isEqualTo(nicknameValue);
        assertThat(model.getBirthday()).isEqualTo(birthday);
        assertThat(model.getPhoneNumber().getValue()).isEqualTo(phoneNumberValue);
        assertThat(model.getProfilePhotoUrl()).isEqualTo("https://placehold.co/100x100");
        verify(passwordEncoder).encode(rawPasswordValue);
    }

    @Test
    void 회원가입_시_비밀번호가_암호화된다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nicknameValue = "테스터";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String phoneNumberValue = "010-1234-5678";

        String encodedPasswordValue = "$2a$10$encodedPassword";
        when(passwordEncoder.encode(rawPasswordValue)).thenReturn(encodedPasswordValue);

        // when
        Model model = Model.signUp(identifierValue, rawPasswordValue, nicknameValue, birthday,
            phoneNumberValue, passwordEncoder);

        // then
        assertThat(model.getPassword().getValue()).isEqualTo(encodedPasswordValue);
        assertThat(model.getPassword().getValue()).isNotEqualTo(rawPasswordValue);
        verify(passwordEncoder).encode(rawPasswordValue);
    }

    @Test
    void 회원가입_시_기본_프로필_사진_URL이_설정된다() {
        // given
        String identifierValue = "testuser";
        String rawPasswordValue = "password1234";
        String nicknameValue = "테스터";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String phoneNumberValue = "010-1234-5678";

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");

        // when
        Model model = Model.signUp(identifierValue, rawPasswordValue, nicknameValue, birthday,
            phoneNumberValue, passwordEncoder);

        // then
        assertThat(model.getProfilePhotoUrl()).isNotNull();
        assertThat(model.getProfilePhotoUrl()).isEqualTo("https://placehold.co/100x100");
    }

    @Test
    void 올바른_비밀번호로_로그인하면_true를_반환한다() {
        // given
        String rawPasswordValue = "password1234";
        String encodedPasswordValue = "$2a$10$encodedPassword";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);
        when(passwordEncoder.matches(eq(rawPasswordValue), eq(encodedPasswordValue))).thenReturn(
            true);

        Model model = Model.signUp("testuser", rawPasswordValue, "테스터",
            LocalDate.of(1990, 1, 1), "010-1234-5678", passwordEncoder);

        // when
        boolean result = model.validatePassword(rawPasswordValue, passwordEncoder);

        // then
        assertThat(result).isTrue();
        verify(passwordEncoder).matches(eq(rawPasswordValue), eq(encodedPasswordValue));
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_false를_반환한다() {        // given
        String correctPassword = "password1234";
        String wrongPassword = "wrongpassword";
        String encodedPasswordValue = "$2a$10$encodedPassword";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPasswordValue);
        when(passwordEncoder.matches(eq(wrongPassword), eq(encodedPasswordValue))).thenReturn(
            false);

        Model model = Model.signUp("testuser", correctPassword, "테스터",
            LocalDate.of(1990, 1, 1), "010-1234-5678", passwordEncoder);

        // when
        boolean result = model.validatePassword(wrongPassword, passwordEncoder);

        // then
        assertThat(result).isFalse();
        verify(passwordEncoder).matches(eq(wrongPassword), eq(encodedPasswordValue));
    }
}
