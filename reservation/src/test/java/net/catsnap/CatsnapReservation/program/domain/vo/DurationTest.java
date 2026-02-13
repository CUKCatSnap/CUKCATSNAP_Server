package net.catsnap.CatsnapReservation.program.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Duration VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DurationTest {

    @Test
    void 유효한_소요시간으로_생성에_성공한다() {
        // given
        Integer value = 90;

        // when
        Duration duration = new Duration(value);

        // then
        assertThat(duration.getValue()).isEqualTo(value);
    }

    @Test
    void 일분으로_생성에_성공한다() {
        // given
        Integer value = 1;

        // when
        Duration duration = new Duration(value);

        // then
        assertThat(duration.getValue()).isEqualTo(1);
    }

    @Test
    void null_소요시간으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Duration(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("소요 시간은 필수입니다");
    }

    @Test
    void 영분으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Duration(0))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("1분 이상");
    }

    @Test
    void 음수_분으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Duration(-1))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("1분 이상");
    }

    @Test
    void toHours가_시간_부분을_반환한다() {
        // given
        Duration duration = new Duration(90);

        // when
        int hours = duration.toHours();

        // then
        assertThat(hours).isEqualTo(1);
    }

    @Test
    void remainingMinutes가_남은_분을_반환한다() {
        // given
        Duration duration = new Duration(90);

        // when
        int remainingMinutes = duration.remainingMinutes();

        // then
        assertThat(remainingMinutes).isEqualTo(30);
    }

    @Test
    void 육십분_미만일_때_toHours가_영을_반환한다() {
        // given
        Duration duration = new Duration(45);

        // when
        int hours = duration.toHours();

        // then
        assertThat(hours).isEqualTo(0);
    }

    @Test
    void addTo가_시작_시각에_소요시간을_더한_시각을_반환한다() {
        // given
        Duration duration = new Duration(90);
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);

        // when
        LocalDateTime endTime = duration.addTo(startTime);

        // then
        assertThat(endTime).isEqualTo(LocalDateTime.of(2025, 6, 15, 11, 30));
    }

    @Test
    void addTo에_null_시작_시각을_전달하면_예외가_발생한다() {
        // given
        Duration duration = new Duration(60);

        // when & then
        assertThatThrownBy(() -> duration.addTo(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("시작 시각은 필수입니다");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        Duration duration1 = new Duration(90);
        Duration duration2 = new Duration(90);

        // when & then
        assertThat(duration1).isEqualTo(duration2);
        assertThat(duration1.hashCode()).isEqualTo(duration2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        Duration duration1 = new Duration(60);
        Duration duration2 = new Duration(90);

        // when & then
        assertThat(duration1).isNotEqualTo(duration2);
    }

    @Test
    void 육십분_미만일_때_toString이_분_단위로_반환한다() {
        // given
        Duration duration = new Duration(45);

        // when
        String result = duration.toString();

        // then
        assertThat(result).isEqualTo("45분");
    }

    @Test
    void 정확히_60분일_때_toString이_시간_단위로_반환한다() {
        // given
        Duration duration = new Duration(60);

        // when
        String result = duration.toString();

        // then
        assertThat(result).isEqualTo("1시간");
    }

    @Test
    void 시간과_분이_있을_때_toString이_둘_다_반환한다() {
        // given
        Duration duration = new Duration(90);

        // when
        String result = duration.toString();

        // then
        assertThat(result).isEqualTo("1시간 30분");
    }

    @Test
    void 정확히_120분일_때_toString이_시간_단위로_반환한다() {
        // given
        Duration duration = new Duration(120);

        // when
        String result = duration.toString();

        // then
        assertThat(result).isEqualTo("2시간");
    }
}
