package net.catsnap.CatsnapReservation.program.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Description VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DescriptionTest {

    @Test
    void 유효한_설명으로_생성에_성공한다() {
        // given
        String value = "아름다운 웨딩 스냅 촬영입니다.";

        // when
        Description description = new Description(value);

        // then
        assertThat(description.getValue()).isEqualTo(value);
    }

    @Test
    void null_설명으로_생성에_성공한다() {
        // when
        Description description = new Description(null);

        // then
        assertThat(description.getValue()).isNull();
        assertThat(description.isEmpty()).isTrue();
    }

    @Test
    void 빈_문자열_설명으로_생성에_성공한다() {
        // when
        Description description = new Description("");

        // then
        assertThat(description.getValue()).isEqualTo("");
        assertThat(description.isEmpty()).isTrue();
    }

    @Test
    void 오백자_설명으로_생성에_성공한다() {
        // given
        String value = "A".repeat(500);

        // when
        Description description = new Description(value);

        // then
        assertThat(description.getValue()).isEqualTo(value);
        assertThat(description.getValue().length()).isEqualTo(500);
    }

    @Test
    void 오백일자_이상_설명으로_생성_시_예외가_발생한다() {
        // given
        String value = "A".repeat(501);

        // when & then
        assertThatThrownBy(() -> new Description(value))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("500자 이하");
    }

    @Test
    void 공백_문자열은_isEmpty가_true를_반환한다() {
        // given
        Description description = new Description("   ");

        // when & then
        assertThat(description.isEmpty()).isTrue();
    }

    @Test
    void 값이_있으면_isEmpty가_false를_반환한다() {
        // given
        Description description = new Description("설명입니다.");

        // when & then
        assertThat(description.isEmpty()).isFalse();
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        Description description1 = new Description("설명");
        Description description2 = new Description("설명");

        // when & then
        assertThat(description1).isEqualTo(description2);
        assertThat(description1.hashCode()).isEqualTo(description2.hashCode());
    }

    @Test
    void null_값을_가진_객체는_같다() {
        // given
        Description description1 = new Description(null);
        Description description2 = new Description(null);

        // when & then
        assertThat(description1).isEqualTo(description2);
    }

    @Test
    void null_값의_toString은_빈_문자열을_반환한다() {
        // given
        Description description = new Description(null);

        // when
        String result = description.toString();

        // then
        assertThat(result).isEqualTo("");
    }

    @Test
    void 값이_있는_toString은_값을_반환한다() {
        // given
        Description description = new Description("설명입니다.");

        // when
        String result = description.toString();

        // then
        assertThat(result).isEqualTo("설명입니다.");
    }
}
