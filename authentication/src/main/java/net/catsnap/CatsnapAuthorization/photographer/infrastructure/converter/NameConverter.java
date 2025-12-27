package net.catsnap.CatsnapAuthorization.photographer.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapAuthorization.photographer.domain.vo.Name;

/**
 * JPA에서 Name 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class NameConverter implements AttributeConverter<Name, String> {

    @Override
    public String convertToDatabaseColumn(Name attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Name convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Name(dbData);
    }
}
