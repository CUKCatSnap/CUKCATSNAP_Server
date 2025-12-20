package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;

/**
 * JPA에서 Identifier 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class IdentifierConverter implements AttributeConverter<Identifier, String> {

    @Override
    public String convertToDatabaseColumn(Identifier attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Identifier convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Identifier(dbData);
    }
}