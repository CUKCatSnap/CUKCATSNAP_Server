package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.program.domain.vo.Title;

/**
 * JPA에서 Title 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class TitleConverter implements AttributeConverter<Title, String> {

    @Override
    public String convertToDatabaseColumn(Title attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Title convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Title(dbData);
    }
}
