package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationNumber VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationNumberTest {

    @Test
    void 유효한_UUID로_생성에_성공한다() {
        // given
        String uuid = UUID.randomUUID().toString();

        // when
        ReservationNumber reservationNumber = new ReservationNumber(uuid);

        // then
        assertThat(reservationNumber.getValue()).isEqualTo(uuid);
    }

    @Test
    void generate로_새로운_예약번호를_생성한다() {
        // when
        ReservationNumber reservationNumber = ReservationNumber.generate();

        // then
        assertThat(reservationNumber.getValue()).isNotBlank();
        UUID.fromString(reservationNumber.getValue()); // UUID 형식 검증
    }

    @Test
    void null로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationNumber(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("예약 번호는 필수입니다");
    }

    @Test
    void 빈_문자열로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationNumber(""))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("예약 번호는 필수입니다");
    }

    @Test
    void 공백_문자열로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationNumber("   "))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("예약 번호는 필수입니다");
    }

    @Test
    void UUID_형식이_아닌_값으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationNumber("invalid-uuid"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("UUID 형식");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        String uuid = UUID.randomUUID().toString();
        ReservationNumber number1 = new ReservationNumber(uuid);
        ReservationNumber number2 = new ReservationNumber(uuid);

        // when & then
        assertThat(number1).isEqualTo(number2);
        assertThat(number1.hashCode()).isEqualTo(number2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        ReservationNumber number1 = ReservationNumber.generate();
        ReservationNumber number2 = ReservationNumber.generate();

        // when & then
        assertThat(number1).isNotEqualTo(number2);
    }

    @Test
    void toString이_UUID_값을_반환한다() {
        // given
        String uuid = UUID.randomUUID().toString();
        ReservationNumber reservationNumber = new ReservationNumber(uuid);

        // when & then
        assertThat(reservationNumber.toString()).isEqualTo(uuid);
    }
}
