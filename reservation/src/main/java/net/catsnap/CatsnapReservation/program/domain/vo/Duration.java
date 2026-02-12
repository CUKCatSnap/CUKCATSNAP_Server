package net.catsnap.CatsnapReservation.program.domain.vo;

import java.util.Objects;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 프로그램 소요 시간 값 객체 (Value Object)
 *
 * <p>프로그램의 소요 시간(분)을 표현하는 불변 객체입니다.
 */
@Getter
public class Duration {

    private static final int MIN_VALUE = 1;

    private final Integer value;

    public Duration(Integer value) {
        validate(value);
        this.value = value;
    }

    private void validate(Integer value) {
        if (value == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "소요 시간은 필수입니다.");
        }
        if (value < MIN_VALUE) {
            String message = String.format("소요 시간은 %d분 이상이어야 합니다. 현재: %d분", MIN_VALUE, value);
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    public int toHours() {
        return value / 60;
    }

    public int remainingMinutes() {
        return value % 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Duration duration = (Duration) o;
        return Objects.equals(value, duration.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (value >= 60) {
            int hours = toHours();
            int minutes = remainingMinutes();
            return minutes > 0 ? hours + "시간 " + minutes + "분" : hours + "시간";
        }
        return value + "분";
    }
}
