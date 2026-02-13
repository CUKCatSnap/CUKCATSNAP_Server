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

/**
 * 요일별 예약 가능 시간 맵을 JSON으로 변환하는 JPA Converter
 * <p>
 * DB: {"MONDAY": ["09:00", "09:30", ...], "TUESDAY": []} Java: Map<DayOfWeek, AvailableStartTimes>
 */
@Converter
public class WeekdayRulesConverter implements
    AttributeConverter<Map<DayOfWeek, AvailableStartTimes>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, AvailableStartTimes> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        try {
            // AvailableStartTimes -> List<String> 변환
            Map<String, List<String>> jsonMap = new HashMap<>();

            for (Map.Entry<DayOfWeek, AvailableStartTimes> entry : attribute.entrySet()) {
                AvailableStartTimes times = entry.getValue();
                List<String> timeStrings = times.toList().stream()
                    .map(LocalTime::toString)
                    .toList();
                jsonMap.put(entry.getKey().name(), timeStrings);
            }

            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("WeekdayRules 맵을 JSON으로 변환 실패", e);
        }
    }

    @Override
    public Map<DayOfWeek, AvailableStartTimes> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new EnumMap<>(DayOfWeek.class);
        }

        try {
            Map<String, List<String>> jsonMap = objectMapper.readValue(
                dbData,
                new TypeReference<Map<String, List<String>>>() {
                }
            );

            Map<DayOfWeek, AvailableStartTimes> result = new EnumMap<>(DayOfWeek.class);

            for (Map.Entry<String, List<String>> entry : jsonMap.entrySet()) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(entry.getKey());
                List<String> timeStrings = entry.getValue();

                if (timeStrings == null || timeStrings.isEmpty()) {
                    result.put(dayOfWeek, AvailableStartTimes.empty());
                } else {
                    List<LocalTime> timeList = timeStrings.stream()
                        .map(LocalTime::parse)
                        .toList();
                    result.put(dayOfWeek, AvailableStartTimes.of(timeList));
                }
            }

            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 WeekdayRules 맵으로 변환 실패", e);
        }
    }
}
