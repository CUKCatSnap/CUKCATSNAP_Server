package net.catsnap.CatsnapReservation.schedule.infrastructure.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;

/**
 * AvailableStartTimes VO를 JSON 배열로 변환하는 JPA Converter
 * <p>
 * DB: ["09:00", "09:30", "10:00", ...] Java: AvailableStartTimes
 */
@Converter
public class AvailableStartTimesConverter implements
    AttributeConverter<AvailableStartTimes, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(AvailableStartTimes attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        try {
            // AvailableStartTimes -> List<String> 변환
            List<String> timeStrings = attribute.toList().stream()
                .map(LocalTime::toString)
                .toList();
            return objectMapper.writeValueAsString(timeStrings);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("AvailableStartTimes를 JSON으로 변환 실패", e);
        }
    }

    @Override
    public AvailableStartTimes convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return AvailableStartTimes.empty();
        }

        try {
            List<String> timeStrings = objectMapper.readValue(
                dbData,
                new TypeReference<List<String>>() {
                }
            );

            if (timeStrings.isEmpty()) {
                return AvailableStartTimes.empty();
            }

            List<LocalTime> times = timeStrings.stream()
                .map(LocalTime::parse)
                .toList();

            return AvailableStartTimes.of(times);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 AvailableStartTimes로 변환 실패", e);
        }
    }
}