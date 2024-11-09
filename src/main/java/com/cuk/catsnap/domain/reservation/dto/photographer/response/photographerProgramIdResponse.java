package com.cuk.catsnap.domain.reservation.dto.photographer.response;

import com.cuk.catsnap.domain.reservation.entity.Program;

public record photographerProgramIdResponse(
    Long photographerProgramId
) {

    public static photographerProgramIdResponse from(Program program) {
        return new photographerProgramIdResponse(program.getId());
    }
}
