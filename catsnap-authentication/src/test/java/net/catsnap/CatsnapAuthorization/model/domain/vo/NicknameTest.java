package net.catsnap.CatsnapAuthorization.model.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Nickname 값 객체 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NicknameTest {

    @Test
    void 정상적인_닉네임으로_생성에_성공한다() {
        // given
        String validValue = "홍길동";

        // when
        Nickname nickname = new Nickname(validValue);

        // then
        assertThat(nickname.getValue()).isEqualTo(validValue);
    }

    @Test
    void null_값으로_생성_시_예외가_발생한다() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new Nickname(nullValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("닉네임은 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 빈_문자열로_생성_시_예외가_발생한다() {
        // given
        String blankValue = "   ";

        // when & then
        assertThatThrownBy(() -> new Nickname(blankValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("닉네임은 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_미만으로_생성_시_예외가_발생한다() {
        // given
        String tooShort = "a"; // 1자

        // when & then
        assertThatThrownBy(() -> new Nickname(tooShort))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("닉네임은 2자 이상 20자 이하여야 합니다")
            .hasMessageContaining("현재: 1자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최대_길이_초과로_생성_시_예외가_발생한다() {
        // given
        String tooLong = "a".repeat(21); // 21자

        // when & then
        assertThatThrownBy(() -> new Nickname(tooLong))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("닉네임은 2자 이상 20자 이하여야 합니다")
            .hasMessageContaining("현재: 21자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_경계값으로_생성에_성공한다() {
        // given
        String minLength = "ab"; // 2자

        // when
        Nickname nickname = new Nickname(minLength);

        // then
        assertThat(nickname.getValue()).isEqualTo(minLength);
    }

    @Test
    void 최대_길이_경계값으로_생성에_성공한다() {
        // given
        String maxLength = "a".repeat(20); // 20자

        // when
        Nickname nickname = new Nickname(maxLength);

        // then
        assertThat(nickname.getValue()).isEqualTo(maxLength);
    }
}
