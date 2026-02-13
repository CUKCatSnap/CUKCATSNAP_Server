package net.catsnap.CatsnapReservation.schedule.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("AvailableStartTimes VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AvailableStartTimesTest {

    @Test
    void 시간_목록으로_생성에_성공한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        );

        // when
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // then
        assertThat(availableTimes.isEmpty()).isFalse();
        assertThat(availableTimes.size()).isEqualTo(3);
        assertThat(availableTimes.toList()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        );
    }

    @Test
    void 빈_시간_목록_생성에_성공한다() {
        // when
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // then
        assertThat(availableTimes.isEmpty()).isTrue();
        assertThat(availableTimes.size()).isEqualTo(0);
        assertThat(availableTimes.toList()).isEmpty();
    }

    @Test
    void null로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> AvailableStartTimes.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능 시간은 null일 수 없습니다.");
    }

    @Test
    void 시간_목록이_자동으로_정렬된다() {
        // given
        List<LocalTime> unsortedTimes = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(9, 0),
            LocalTime.of(11, 30)
        );

        // when
        AvailableStartTimes availableTimes = AvailableStartTimes.of(unsortedTimes);

        // then
        assertThat(availableTimes.toList()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(11, 30),
            LocalTime.of(14, 0)
        );
    }

    @Test
    void 중복된_시간이_자동으로_제거된다() {
        // given
        List<LocalTime> duplicatedTimes = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );

        // when
        AvailableStartTimes availableTimes = AvailableStartTimes.of(duplicatedTimes);

        // then
        assertThat(availableTimes.size()).isEqualTo(2);
        assertThat(availableTimes.toList()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
    }

    @Test
    void 특정_시간이_포함되어_있는지_확인할_수_있다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // when & then
        assertThat(availableTimes.contains(LocalTime.of(9, 0))).isTrue();
        assertThat(availableTimes.contains(LocalTime.of(10, 0))).isTrue();
        assertThat(availableTimes.contains(LocalTime.of(11, 0))).isFalse();
    }

    @Test
    void 첫_번째_시간을_반환한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(9, 0),
            LocalTime.of(11, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // when
        LocalTime first = availableTimes.first();

        // then
        assertThat(first).isEqualTo(LocalTime.of(9, 0));
    }

    @Test
    void 마지막_시간을_반환한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(14, 0),
            LocalTime.of(9, 0),
            LocalTime.of(11, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // when
        LocalTime last = availableTimes.last();

        // then
        assertThat(last).isEqualTo(LocalTime.of(14, 0));
    }

    @Test
    void 빈_목록에서_첫_번째_시간을_조회하면_예외가_발생한다() {
        // given
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(availableTimes::first)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("예약 가능 시간이 없습니다.");
    }

    @Test
    void 빈_목록에서_마지막_시간을_조회하면_예외가_발생한다() {
        // given
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when & then
        assertThatThrownBy(availableTimes::last)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("예약 가능 시간이 없습니다.");
    }

    @Test
    void 동일한_시간_목록을_가진_객체는_같다() {
        // given
        List<LocalTime> times = List.of(LocalTime.of(9, 0), LocalTime.of(10, 0));
        AvailableStartTimes availableTimes1 = AvailableStartTimes.of(times);
        AvailableStartTimes availableTimes2 = AvailableStartTimes.of(times);

        // when & then
        assertThat(availableTimes1).isEqualTo(availableTimes2);
        assertThat(availableTimes1.hashCode()).isEqualTo(availableTimes2.hashCode());
    }

    @Test
    void toString이_올바르게_동작한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // when
        String result = availableTimes.toString();

        // then
        assertThat(result).contains("09:00");
        assertThat(result).contains("11:00");
        assertThat(result).contains("count=3");
    }

    @Test
    void 빈_목록의_toString이_올바르게_동작한다() {
        // given
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when
        String result = availableTimes.toString();

        // then
        assertThat(result).contains("empty");
    }
}