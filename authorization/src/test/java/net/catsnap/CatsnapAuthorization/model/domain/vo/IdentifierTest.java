package net.catsnap.CatsnapAuthorization.model.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.shared.exception.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Identifier 값 객체 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class IdentifierTest {

    @Test
    void 정상적인_식별자로_생성에_성공한다() {
        // given
        String validValue = "testuser";

        // when
        Identifier identifier = new Identifier(validValue);

        // then
        assertThat(identifier.getValue()).isEqualTo(validValue);
    }

    @Test
    void null_값으로_생성_시_예외가_발생한다() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new Identifier(nullValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("식별자는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 빈_문자열로_생성_시_예외가_발생한다() {
        // given
        String blankValue = "   ";

        // when & then
        assertThatThrownBy(() -> new Identifier(blankValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("식별자는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_미만으로_생성_시_예외가_발생한다() {
        // given
        String tooShort = "abc"; // 3자

        // when & then
        assertThatThrownBy(() -> new Identifier(tooShort))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("식별자는 4자 이상 20자 이하여야 합니다")
            .hasMessageContaining("현재: 3자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최대_길이_초과로_생성_시_예외가_발생한다() {
        // given
        String tooLong = "a".repeat(21); // 21자

        // when & then
        assertThatThrownBy(() -> new Identifier(tooLong))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("식별자는 4자 이상 20자 이하여야 합니다")
            .hasMessageContaining("현재: 21자")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 최소_길이_경계값으로_생성에_성공한다() {
        // given
        String minLength = "abcd"; // 4자

        // when
        Identifier identifier = new Identifier(minLength);

        // then
        assertThat(identifier.getValue()).isEqualTo(minLength);
    }

    @Test
    void 최대_길이_경계값으로_생성에_성공한다() {
        // given
        String maxLength = "a".repeat(20); // 20자

        // when
        Identifier identifier = new Identifier(maxLength);

        // then
        assertThat(identifier.getValue()).isEqualTo(maxLength);
    }
}
