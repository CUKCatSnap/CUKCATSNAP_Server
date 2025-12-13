package net.catsnap.CatsnapAuthorization.model.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("RawPassword 값 객체 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RawPasswordTest {

    @Test
    void 정상적인_비밀번호로_생성에_성공한다() {
        // given
        String validValue = "password1234";

        // when
        RawPassword rawPassword = new RawPassword(validValue);

        // then
        assertThat(rawPassword.getValue()).isEqualTo(validValue);
    }

    @Test
    void null_값으로_생성_시_예외가_발생한다() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new RawPassword(nullValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("비밀번호는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 빈_문자열로_생성_시_예외가_발생한다() {
        // given
        String blankValue = "   ";

        // when & then
        assertThatThrownBy(() -> new RawPassword(blankValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("비밀번호는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_미만으로_생성_시_예외가_발생한다() {
        // given
        String tooShort = "pass"; // 4자

        // when & then
        assertThatThrownBy(() -> new RawPassword(tooShort))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("비밀번호는 최소 8자 이상이어야 합니다")
            .hasMessageContaining("현재: 4자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최대_길이_초과로_생성_시_예외가_발생한다() {
        // given
        String tooLong = "a".repeat(65); // 65자

        // when & then
        assertThatThrownBy(() -> new RawPassword(tooLong))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("비밀번호는 최대 64자 이하여야 합니다")
            .hasMessageContaining("현재: 65자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_경계값으로_생성에_성공한다() {
        // given
        String minLength = "a".repeat(8); // 8자

        // when
        RawPassword rawPassword = new RawPassword(minLength);

        // then
        assertThat(rawPassword.getValue()).isEqualTo(minLength);
    }

    @Test
    void 최대_길이_경계값으로_생성에_성공한다() {
        // given
        String maxLength = "a".repeat(64); // 64자

        // when
        RawPassword rawPassword = new RawPassword(maxLength);

        // then
        assertThat(rawPassword.getValue()).isEqualTo(maxLength);
    }

    @Test
    void toString은_보안을_위해_마스킹된_문자열을_반환한다() {
        // given
        String validValue = "password1234";
        RawPassword rawPassword = new RawPassword(validValue);

        // when
        String result = rawPassword.toString();

        // then
        assertThat(result).doesNotContain(validValue);
    }
}
