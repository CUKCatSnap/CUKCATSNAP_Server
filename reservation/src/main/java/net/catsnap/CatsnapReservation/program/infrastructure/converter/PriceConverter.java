package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.program.domain.vo.Price;

/**
 * JPA에서 Price 값 객체를 Long으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, Long> {

    @Override
    public Long convertToDatabaseColumn(Price attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Price convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new Price(dbData);
    }
}
