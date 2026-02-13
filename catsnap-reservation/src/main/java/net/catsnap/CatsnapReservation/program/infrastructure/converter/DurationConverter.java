package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.program.domain.vo.Duration;

/**
 * JPA에서 Duration 값 객체를 Integer로 변환하는 Converter
 */
@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Duration attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Duration convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : new Duration(dbData);
    }
}
