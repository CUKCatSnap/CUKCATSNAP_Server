package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationNumberConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationNumberConverterTest {

    private ReservationNumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ReservationNumberConverter();
    }

    @Test
    void ReservationNumber를_String으로_변환한다() {
        // given
        String uuid = UUID.randomUUID().toString();
        ReservationNumber reservationNumber = new ReservationNumber(uuid);

        // when
        String result = converter.convertToDatabaseColumn(reservationNumber);

        // then
        assertThat(result).isEqualTo(uuid);
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void String을_ReservationNumber로_변환한다() {
        // given
        String uuid = UUID.randomUUID().toString();

        // when
        ReservationNumber result = converter.convertToEntityAttribute(uuid);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(uuid);
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        ReservationNumber result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        ReservationNumber original = ReservationNumber.generate();

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        ReservationNumber restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }
}
