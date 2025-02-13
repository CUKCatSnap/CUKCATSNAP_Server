package net.catsnap.domain.reservation.dto;

import net.catsnap.domain.reservation.entity.Program;

public record PhotographerProgramResponse(
    Long programId,
    String title,
    String content,
    Long price,
    Long durationMinutes
) {

    public static PhotographerProgramResponse from(Program program) {
        return new PhotographerProgramResponse(
            program.getId(),
            program.getTitle(),
            program.getContent(),
            program.getPrice(),
            program.getDurationMinutes()
        );
    }
}
