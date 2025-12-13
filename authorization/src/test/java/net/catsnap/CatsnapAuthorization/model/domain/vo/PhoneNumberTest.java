package net.catsnap.CatsnapAuthorization.model.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PhoneNumber 값 객체 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhoneNumberTest {

    @Test
    void 정상적인_전화번호로_생성에_성공한다() {
        // given
        String validValue = "010-1234-5678";

        // when
        PhoneNumber phoneNumber = new PhoneNumber(validValue);

        // then
        assertThat(phoneNumber.getValue()).isEqualTo(validValue);
    }

    @Test
    void 잘못된_형식으로_생성_시_예외가_발생한다_하이픈_없음() {
        // given
        String invalidFormat = "01012345678";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(invalidFormat))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("전화번호는 010-XXXX-XXXX 형식이어야 합니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 잘못된_형식으로_생성_시_예외가_발생한다_010이_아님() {
        // given
        String invalidFormat = "011-1234-5678";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(invalidFormat))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("전화번호는 010-XXXX-XXXX 형식이어야 합니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 잘못된_형식으로_생성_시_예외가_발생한다_중간_자리수_부족() {
        // given
        String invalidFormat = "010-123-5678";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(invalidFormat))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("전화번호는 010-XXXX-XXXX 형식이어야 합니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 잘못된_형식으로_생성_시_예외가_발생한다_마지막_자리수_부족() {
        // given
        String invalidFormat = "010-1234-567";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(invalidFormat))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("전화번호는 010-XXXX-XXXX 형식이어야 합니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 잘못된_형식으로_생성_시_예외가_발생한다_문자_포함() {
        // given
        String invalidFormat = "010-abcd-5678";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(invalidFormat))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("전화번호는 010-XXXX-XXXX 형식이어야 합니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void toString은_보안을_위해_마스킹된_문자열을_반환한다() {
        // given
        String validValue = "010-1234-5678";
        PhoneNumber phoneNumber = new PhoneNumber(validValue);

        // when
        String result = phoneNumber.toString();

        // then
        assertThat(result).isEqualTo("***-****-****");
        assertThat(result).doesNotContain("1234");
        assertThat(result).doesNotContain("5678");
    }
}
