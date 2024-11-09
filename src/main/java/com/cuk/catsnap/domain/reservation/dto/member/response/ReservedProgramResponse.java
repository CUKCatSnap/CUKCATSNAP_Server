package com.cuk.catsnap.domain.reservation.dto.member.response;

import com.cuk.catsnap.domain.reservation.entity.Program;

public record ReservedProgramResponse(
    String title,
    String content,
    Long durationMinutes,
    Long price
) {

    public static ReservedProgramResponse from(Program program) {
        return new ReservedProgramResponse(
            program.getTitle(),
            program.getContent(),
            program.getDurationMinutes(),
            program.getPrice()
        );
    }
}
