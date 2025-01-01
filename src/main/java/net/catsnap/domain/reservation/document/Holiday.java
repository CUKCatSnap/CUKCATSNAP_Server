package net.catsnap.domain.reservation.document;

import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Holiday", timeToLive = 60 * 60 * 24 * 365) // 365일
@NoArgsConstructor
@Getter
public class Holiday {

    @Id
    private String id;

    private String holidayName;

    public Holiday(LocalDate date, String holidayName) {
        this.id = date.toString();
        this.holidayName = holidayName;
    }

    public LocalDate idToLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(this.id, formatter);
    }
}
