package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.reservation.domain.vo.Money;

/**
 * JPA에서 Money 값 객체를 Long으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(Money attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Money convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new Money(dbData);
    }
}
