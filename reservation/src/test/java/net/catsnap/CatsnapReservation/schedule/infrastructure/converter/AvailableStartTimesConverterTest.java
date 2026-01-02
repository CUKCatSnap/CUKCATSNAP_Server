package net.catsnap.CatsnapReservation.schedule.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("AvailableStartTimesConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AvailableStartTimesConverterTest {

    private AvailableStartTimesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AvailableStartTimesConverter();
    }

    @Test
    void AvailableStartTimes를_JSON_문자열로_변환한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
        AvailableStartTimes availableTimes = AvailableStartTimes.of(times);

        // when
        String json = converter.convertToDatabaseColumn(availableTimes);

        // then
        assertThat(json).isNotNull();
        assertThat(json).contains("09:00");
        assertThat(json).contains("10:30");
        assertThat(json).contains("14:00");
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String json = converter.convertToDatabaseColumn(null);

        // then
        assertThat(json).isNull();
    }

    @Test
    void 빈_AvailableStartTimes를_DB_컬럼으로_변환하면_null을_반환한다() {
        // given
        AvailableStartTimes availableTimes = AvailableStartTimes.empty();

        // when
        String json = converter.convertToDatabaseColumn(availableTimes);

        // then
        assertThat(json).isNull();
    }

    @Test
    void JSON_문자열을_AvailableStartTimes로_변환한다() {
        // given
        String json = "[\"09:00\",\"10:30\",\"14:00\"]";

        // when
        AvailableStartTimes availableTimes = converter.convertToEntityAttribute(json);

        // then
        assertThat(availableTimes).isNotNull();
        assertThat(availableTimes.isEmpty()).isFalse();
        assertThat(availableTimes.size()).isEqualTo(3);
        assertThat(availableTimes.toList()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
    }

    @Test
    void null_DB_데이터를_엔티티_속성으로_변환하면_빈_AvailableStartTimes를_반환한다() {
        // when
        AvailableStartTimes availableTimes = converter.convertToEntityAttribute(null);

        // then
        assertThat(availableTimes).isNotNull();
        assertThat(availableTimes.isEmpty()).isTrue();
    }

    @Test
    void 빈_문자열_DB_데이터를_엔티티_속성으로_변환하면_빈_AvailableStartTimes를_반환한다() {
        // when
        AvailableStartTimes availableTimes = converter.convertToEntityAttribute("");

        // then
        assertThat(availableTimes).isNotNull();
        assertThat(availableTimes.isEmpty()).isTrue();
    }

    @Test
    void 공백_문자열_DB_데이터를_엔티티_속성으로_변환하면_빈_AvailableStartTimes를_반환한다() {
        // when
        AvailableStartTimes availableTimes = converter.convertToEntityAttribute("   ");

        // then
        assertThat(availableTimes).isNotNull();
        assertThat(availableTimes.isEmpty()).isTrue();
    }

    @Test
    void 빈_배열_JSON을_엔티티_속성으로_변환하면_빈_AvailableStartTimes를_반환한다() {
        // given
        String json = "[]";

        // when
        AvailableStartTimes availableTimes = converter.convertToEntityAttribute(json);

        // then
        assertThat(availableTimes).isNotNull();
        assertThat(availableTimes.isEmpty()).isTrue();
    }

    @Test
    void 잘못된_JSON_형식이면_예외가_발생한다() {
        // given
        String invalidJson = "invalid json";

        // when & then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidJson))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("JSON을 AvailableStartTimes로 변환 실패");
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(13, 30),
            LocalTime.of(16, 0)
        );
        AvailableStartTimes original = AvailableStartTimes.of(times);

        // when
        String json = converter.convertToDatabaseColumn(original);
        AvailableStartTimes restored = converter.convertToEntityAttribute(json);

        // then
        assertThat(restored).isEqualTo(original);
        assertThat(restored.toList()).containsExactlyElementsOf(original.toList());
    }
}