package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;

/**
 * JPA에서 CancelReason 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class CancelReasonConverter implements AttributeConverter<CancelReason, String> {

    @Override
    public String convertToDatabaseColumn(CancelReason attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public CancelReason convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new CancelReason(dbData);
    }
}
