package net.catsnap.CatsnapReservation.program.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Title VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TitleTest {

    @Test
    void 유효한_제목으로_생성에_성공한다() {
        // given
        String value = "웨딩 스냅 촬영";

        // when
        Title title = new Title(value);

        // then
        assertThat(title.getValue()).isEqualTo(value);
    }

    @Test
    void 일자_제목으로_생성에_성공한다() {
        // given
        String value = "A";

        // when
        Title title = new Title(value);

        // then
        assertThat(title.getValue()).isEqualTo(value);
    }

    @Test
    void 백자_제목으로_생성에_성공한다() {
        // given
        String value = "A".repeat(100);

        // when
        Title title = new Title(value);

        // then
        assertThat(title.getValue()).isEqualTo(value);
        assertThat(title.getValue().length()).isEqualTo(100);
    }

    @Test
    void null_제목으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Title(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("프로그램 제목은 필수입니다");
    }

    @Test
    void 빈_문자열_제목으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Title(""))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("프로그램 제목은 필수입니다");
    }

    @Test
    void 공백_문자열_제목으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Title("   "))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("프로그램 제목은 필수입니다");
    }

    @Test
    void 백일자_이상_제목으로_생성_시_예외가_발생한다() {
        // given
        String value = "A".repeat(101);

        // when & then
        assertThatThrownBy(() -> new Title(value))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("100자 이하");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        Title title1 = new Title("웨딩 스냅");
        Title title2 = new Title("웨딩 스냅");

        // when & then
        assertThat(title1).isEqualTo(title2);
        assertThat(title1.hashCode()).isEqualTo(title2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        Title title1 = new Title("웨딩 스냅");
        Title title2 = new Title("프로필 촬영");

        // when & then
        assertThat(title1).isNotEqualTo(title2);
    }

    @Test
    void toString이_값을_반환한다() {
        // given
        Title title = new Title("웨딩 스냅");

        // when
        String result = title.toString();

        // then
        assertThat(result).isEqualTo("웨딩 스냅");
    }
}
