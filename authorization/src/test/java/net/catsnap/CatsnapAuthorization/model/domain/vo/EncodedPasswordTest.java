package net.catsnap.CatsnapAuthorization.model.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapAuthorization.shared.exception.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("EncodedPassword 값 객체 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EncodedPasswordTest {

    @Test
    void 정상적인_암호화된_비밀번호로_생성에_성공한다() {
        // given
        String encodedValue = "$2a$10$abcdefghijklmnopqrstuv";

        // when
        EncodedPassword encodedPassword = new EncodedPassword(encodedValue);

        // then
        assertThat(encodedPassword.getValue()).isEqualTo(encodedValue);
    }

    @Test
    void null_값으로_생성_시_예외가_발생한다() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new EncodedPassword(nullValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("암호화된 비밀번호는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void 빈_문자열로_생성_시_예외가_발생한다() {
        // given
        String blankValue = "   ";

        // when & then
        assertThatThrownBy(() -> new EncodedPassword(blankValue))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("암호화된 비밀번호는 필수입니다")
            .extracting("resultCode")
            .isEqualTo(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION);
    }

    @Test
    void toString은_보안을_위해_마스킹된_문자열을_반환한다() {
        // given
        String encodedValue = "$2a$10$abcdefghijklmnopqrstuv";
        EncodedPassword encodedPassword = new EncodedPassword(encodedValue);

        // when
        String result = encodedPassword.toString();

        // then
        assertThat(result).doesNotContain(encodedValue);
    }
}
