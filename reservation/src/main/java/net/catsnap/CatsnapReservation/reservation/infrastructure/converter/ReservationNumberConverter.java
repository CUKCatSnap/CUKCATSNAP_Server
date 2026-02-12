package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationNumber;

/**
 * JPA에서 ReservationNumber 값 객체를 String으로 변환하는 Converter
 */
@Converter(autoApply = true)
public class ReservationNumberConverter implements AttributeConverter<ReservationNumber, String> {

    @Override
    public String convertToDatabaseColumn(ReservationNumber attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public ReservationNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new ReservationNumber(dbData);
    }
}
