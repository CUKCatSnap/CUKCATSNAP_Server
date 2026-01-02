package net.catsnap.CatsnapReservation.schedule.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.catsnap.CatsnapReservation.schedule.domain.vo.WeekdayScheduleRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("WeekdayRulesConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WeekdayRulesConverterTest {

    private WeekdayRulesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new WeekdayRulesConverter();
    }

    @Test
    void WeekdayRules_맵을_JSON_문자열로_변환한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> rules = new EnumMap<>(DayOfWeek.class);
        List<LocalTime> mondayTimes = List.of(LocalTime.of(9, 0), LocalTime.of(10, 0));
        rules.put(DayOfWeek.MONDAY, WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, mondayTimes));
        rules.put(DayOfWeek.TUESDAY, WeekdayScheduleRule.dayOff(DayOfWeek.TUESDAY));

        // when
        String json = converter.convertToDatabaseColumn(rules);

        // then
        assertThat(json).isNotNull();
        assertThat(json).contains("MONDAY");
        assertThat(json).contains("TUESDAY");
        assertThat(json).contains("isWorkingDay");
        assertThat(json).contains("09:00");
        assertThat(json).contains("10:00");
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String json = converter.convertToDatabaseColumn(null);

        // then
        assertThat(json).isNull();
    }

    @Test
    void 빈_맵을_DB_컬럼으로_변환하면_null을_반환한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> emptyRules = new EnumMap<>(DayOfWeek.class);

        // when
        String json = converter.convertToDatabaseColumn(emptyRules);

        // then
        assertThat(json).isNull();
    }

    @Test
    void 근무일_규칙을_JSON으로_변환한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> rules = new EnumMap<>(DayOfWeek.class);
        List<LocalTime> times = List.of(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30),
            LocalTime.of(14, 0)
        );
        rules.put(DayOfWeek.WEDNESDAY, WeekdayScheduleRule.workingDay(DayOfWeek.WEDNESDAY, times));

        // when
        String json = converter.convertToDatabaseColumn(rules);

        // then
        assertThat(json).contains("WEDNESDAY");
        assertThat(json).contains("\"isWorkingDay\":true");
        assertThat(json).contains("availableStartTimes");
        assertThat(json).contains("09:00");
        assertThat(json).contains("10:30");
        assertThat(json).contains("14:00");
    }

    @Test
    void 휴무일_규칙을_JSON으로_변환한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> rules = new EnumMap<>(DayOfWeek.class);
        rules.put(DayOfWeek.SUNDAY, WeekdayScheduleRule.dayOff(DayOfWeek.SUNDAY));

        // when
        String json = converter.convertToDatabaseColumn(rules);

        // then
        assertThat(json).contains("SUNDAY");
        assertThat(json).contains("\"isWorkingDay\":false");
        assertThat(json).doesNotContain("availableStartTimes");
    }

    @Test
    void JSON_문자열을_WeekdayRules_맵으로_변환한다() {
        // given
        String json = """
            {
              "MONDAY": {
                "dayOfWeek": "MONDAY",
                "isWorkingDay": true,
                "availableStartTimes": ["09:00", "10:00"]
              },
              "TUESDAY": {
                "dayOfWeek": "TUESDAY",
                "isWorkingDay": false
              }
            }
            """;

        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute(json);

        // then
        assertThat(rules).isNotNull();
        assertThat(rules).hasSize(2);

        WeekdayScheduleRule mondayRule = rules.get(DayOfWeek.MONDAY);
        assertThat(mondayRule).isNotNull();
        assertThat(mondayRule.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(mondayRule.isWorkingDay()).isTrue();
        assertThat(mondayRule.generateAvailableTimes()).containsExactly(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0)
        );

        WeekdayScheduleRule tuesdayRule = rules.get(DayOfWeek.TUESDAY);
        assertThat(tuesdayRule).isNotNull();
        assertThat(tuesdayRule.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(tuesdayRule.isWorkingDay()).isFalse();
    }

    @Test
    void null_DB_데이터를_엔티티_속성으로_변환하면_빈_맵을_반환한다() {
        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute(null);

        // then
        assertThat(rules).isNotNull();
        assertThat(rules).isEmpty();
    }

    @Test
    void 빈_문자열_DB_데이터를_엔티티_속성으로_변환하면_빈_맵을_반환한다() {
        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute("");

        // then
        assertThat(rules).isNotNull();
        assertThat(rules).isEmpty();
    }

    @Test
    void 공백_문자열_DB_데이터를_엔티티_속성으로_변환하면_빈_맵을_반환한다() {
        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute("   ");

        // then
        assertThat(rules).isNotNull();
        assertThat(rules).isEmpty();
    }

    @Test
    void 근무일_JSON을_엔티티_속성으로_변환한다() {
        // given
        String json = """
            {
              "FRIDAY": {
                "dayOfWeek": "FRIDAY",
                "isWorkingDay": true,
                "availableStartTimes": ["14:00", "15:30", "17:00"]
              }
            }
            """;

        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute(json);

        // then
        assertThat(rules).hasSize(1);
        WeekdayScheduleRule fridayRule = rules.get(DayOfWeek.FRIDAY);
        assertThat(fridayRule.isWorkingDay()).isTrue();
        assertThat(fridayRule.generateAvailableTimes()).containsExactly(
            LocalTime.of(14, 0),
            LocalTime.of(15, 30),
            LocalTime.of(17, 0)
        );
    }

    @Test
    void 휴무일_JSON을_엔티티_속성으로_변환한다() {
        // given
        String json = """
            {
              "SATURDAY": {
                "dayOfWeek": "SATURDAY",
                "isWorkingDay": false
              }
            }
            """;

        // when
        Map<DayOfWeek, WeekdayScheduleRule> rules = converter.convertToEntityAttribute(json);

        // then
        assertThat(rules).hasSize(1);
        WeekdayScheduleRule saturdayRule = rules.get(DayOfWeek.SATURDAY);
        assertThat(saturdayRule.isWorkingDay()).isFalse();
        assertThat(saturdayRule.generateAvailableTimes()).isEmpty();
    }

    @Test
    void 잘못된_JSON_형식이면_예외가_발생한다() {
        // given
        String invalidJson = "invalid json";

        // when & then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidJson))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("JSON을 WeekdayRules 맵으로 변환 실패");
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> original = new EnumMap<>(DayOfWeek.class);
        List<LocalTime> mondayTimes = List.of(LocalTime.of(9, 0), LocalTime.of(10, 0));
        List<LocalTime> wednesdayTimes = List.of(LocalTime.of(14, 0), LocalTime.of(15, 0));

        original.put(DayOfWeek.MONDAY, WeekdayScheduleRule.workingDay(DayOfWeek.MONDAY, mondayTimes));
        original.put(DayOfWeek.TUESDAY, WeekdayScheduleRule.dayOff(DayOfWeek.TUESDAY));
        original.put(DayOfWeek.WEDNESDAY, WeekdayScheduleRule.workingDay(DayOfWeek.WEDNESDAY, wednesdayTimes));

        // when
        String json = converter.convertToDatabaseColumn(original);
        Map<DayOfWeek, WeekdayScheduleRule> restored = converter.convertToEntityAttribute(json);

        // then
        assertThat(restored).hasSize(3);
        assertThat(restored.get(DayOfWeek.MONDAY)).isEqualTo(original.get(DayOfWeek.MONDAY));
        assertThat(restored.get(DayOfWeek.TUESDAY)).isEqualTo(original.get(DayOfWeek.TUESDAY));
        assertThat(restored.get(DayOfWeek.WEDNESDAY)).isEqualTo(original.get(DayOfWeek.WEDNESDAY));
    }

    @Test
    void 모든_요일_규칙을_포함한_맵을_변환한다() {
        // given
        Map<DayOfWeek, WeekdayScheduleRule> rules = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                rules.put(day, WeekdayScheduleRule.dayOff(day));
            } else {
                List<LocalTime> times = List.of(LocalTime.of(9, 0), LocalTime.of(14, 0));
                rules.put(day, WeekdayScheduleRule.workingDay(day, times));
            }
        }

        // when
        String json = converter.convertToDatabaseColumn(rules);
        Map<DayOfWeek, WeekdayScheduleRule> restored = converter.convertToEntityAttribute(json);

        // then
        assertThat(restored).hasSize(7);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertThat(restored.get(day)).isEqualTo(rules.get(day));
        }
    }
}