package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapAuthorization.model.domain.vo.EncodedPassword;

/**
 * JPA에서 EncodedPassword 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class EncodedPasswordConverter implements AttributeConverter<EncodedPassword, String> {

    @Override
    public String convertToDatabaseColumn(EncodedPassword attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new EncodedPassword(dbData);
    }
}