package net.catsnap.domain.reservation.dto.photographer.response;

import net.catsnap.domain.reservation.entity.Program;

public record photographerProgramIdResponse(
    Long photographerProgramId
) {

    public static photographerProgramIdResponse from(Program program) {
        return new photographerProgramIdResponse(program.getId());
    }
}
