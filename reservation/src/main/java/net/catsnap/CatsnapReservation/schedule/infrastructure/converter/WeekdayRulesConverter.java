package net.catsnap.CatsnapReservation.schedule.infrastructure.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import net.catsnap.CatsnapReservation.schedule.domain.vo.WeekdayScheduleRule;

/**
 * 요일별 스케줄 규칙 맵을 JSON으로 변환하는 JPA Converter
 * <p>
 * DB: {"MONDAY": {"isWorkingDay": true, "availableStartTimes": ["09:00", "09:30", ...]}} Java:
 * Map<DayOfWeek, WeekdayScheduleRule>
 */
@Converter
public class WeekdayRulesConverter implements
    AttributeConverter<Map<DayOfWeek, WeekdayScheduleRule>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, WeekdayScheduleRule> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        try {
            // WeekdayScheduleRule -> Map 변환
            Map<String, Map<String, Object>> jsonMap = new HashMap<>();

            for (Map.Entry<DayOfWeek, WeekdayScheduleRule> entry : attribute.entrySet()) {
                WeekdayScheduleRule rule = entry.getValue();
                Map<String, Object> ruleMap = new HashMap<>();

                ruleMap.put("isWorkingDay", rule.isWorkingDay());

                if (rule.isWorkingDay() && !rule.getAvailableStartTimes().isEmpty()) {
                    List<String> timeStrings = rule.getAvailableStartTimes().toList().stream()
                        .map(LocalTime::toString)
                        .toList();
                    ruleMap.put("availableStartTimes", timeStrings);
                }

                jsonMap.put(entry.getKey().name(), ruleMap);
            }

            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("WeekdayRules 맵을 JSON으로 변환 실패", e);
        }
    }

    @Override
    public Map<DayOfWeek, WeekdayScheduleRule> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new EnumMap<>(DayOfWeek.class);
        }

        try {
            Map<String, Map<String, Object>> jsonMap = objectMapper.readValue(
                dbData,
                new TypeReference<Map<String, Map<String, Object>>>() {
                }
            );

            Map<DayOfWeek, WeekdayScheduleRule> result = new EnumMap<>(DayOfWeek.class);

            for (Map.Entry<String, Map<String, Object>> entry : jsonMap.entrySet()) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(entry.getKey());
                Map<String, Object> ruleMap = entry.getValue();

                boolean isWorkingDay = (Boolean) ruleMap.get("isWorkingDay");

                if (!isWorkingDay) {
                    result.put(dayOfWeek, WeekdayScheduleRule.dayOff());
                } else {
                    @SuppressWarnings("unchecked")
                    List<String> timeStrings = (List<String>) ruleMap.get("availableStartTimes");

                    if (timeStrings != null && !timeStrings.isEmpty()) {
                        List<LocalTime> timeList = timeStrings.stream()
                            .map(LocalTime::parse)
                            .toList();

                        AvailableStartTimes availableStartTimes = AvailableStartTimes.of(timeList);

                        result.put(dayOfWeek, WeekdayScheduleRule.workingDay(availableStartTimes));
                    }
                }
            }

            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 WeekdayRules 맵으로 변환 실패", e);
        }
    }
}
