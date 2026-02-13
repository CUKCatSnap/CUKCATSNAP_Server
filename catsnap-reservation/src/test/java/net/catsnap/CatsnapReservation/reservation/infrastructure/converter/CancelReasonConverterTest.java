package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CancelReasonConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CancelReasonConverterTest {

    private CancelReasonConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CancelReasonConverter();
    }

    @Test
    void CancelReason을_String으로_변환한다() {
        // given
        CancelReason reason = new CancelReason("개인 사정");

        // when
        String result = converter.convertToDatabaseColumn(reason);

        // then
        assertThat(result).isEqualTo("개인 사정");
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void String을_CancelReason으로_변환한다() {
        // given
        String dbData = "개인 사정";

        // when
        CancelReason result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo("개인 사정");
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        CancelReason result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        CancelReason original = new CancelReason("일정 변경으로 인한 취소");

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        CancelReason restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }
}
