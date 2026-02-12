package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.program.domain.vo.Description;

/**
 * JPA에서 Description 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class DescriptionConverter implements AttributeConverter<Description, String> {

    @Override
    public String convertToDatabaseColumn(Description attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Description convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Description(dbData);
    }
}
