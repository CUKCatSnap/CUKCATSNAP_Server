package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Nickname;

/**
 * JPA에서 Nickname 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class NicknameConverter implements AttributeConverter<Nickname, String> {

    @Override
    public String convertToDatabaseColumn(Nickname attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Nickname convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Nickname(dbData);
    }
}