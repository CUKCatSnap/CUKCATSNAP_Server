package com.cuk.catsnap.domain.reservation.document;

import jakarta.persistence.Id;
import java.time.LocalDate;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Holiday", timeToLive = 60 * 60 * 24 * 365) // 365일
public class Holiday {

    @Id
    private String id;

    public Holiday(LocalDate date) {
        this.id = date.toString();
        this.id = id;
    }
}
