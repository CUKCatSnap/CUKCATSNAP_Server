package net.catsnap.CatsnapReservation.program.fixture;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.program.domain.Program;

/**
 * Program 테스트용 Fixture
 */
public class ProgramFixture {

    public static final Long DEFAULT_PHOTOGRAPHER_ID = 1L;
    public static final String DEFAULT_TITLE = "기본 프로그램";
    public static final String DEFAULT_DESCRIPTION = "기본 설명";
    public static final Long DEFAULT_PRICE = 100000L;
    public static final Integer DEFAULT_DURATION = 60;

    /**
     * 기본 프로그램 생성
     */
    public static Program createDefault() {
        return Program.create(
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_TITLE,
            DEFAULT_DESCRIPTION,
            DEFAULT_PRICE,
            DEFAULT_DURATION
        );
    }

    /**
     * 지정된 photographerId로 프로그램 생성
     */
    public static Program createWithPhotographerId(Long photographerId) {
        return Program.create(
            photographerId,
            DEFAULT_TITLE,
            DEFAULT_DESCRIPTION,
            DEFAULT_PRICE,
            DEFAULT_DURATION
        );
    }

    /**
     * 커스텀 값으로 프로그램 생성
     */
    public static Program create(
        Long photographerId,
        String title,
        String description,
        Long price,
        Integer durationMinutes
    ) {
        return Program.create(photographerId, title, description, price, durationMinutes);
    }

    /**
     * 삭제된 프로그램 생성
     */
    public static Program createDeleted(Long photographerId) {
        Program program = createWithPhotographerId(photographerId);
        program.delete(LocalDateTime.of(2024, 1, 1, 12, 0));
        return program;
    }

    /**
     * 삭제된 기본 프로그램 생성
     */
    public static Program createDefaultDeleted() {
        return createDeleted(DEFAULT_PHOTOGRAPHER_ID);
    }
}