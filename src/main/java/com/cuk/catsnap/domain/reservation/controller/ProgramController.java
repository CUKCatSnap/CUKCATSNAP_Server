package com.cuk.catsnap.domain.reservation.controller;

import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.service.ProgramService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.ReservationResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예약 프로그램 관련 API", description = "사용자가 프로그램을 조회하거나 작가가 프로그램을 등록 수정하는 API입니다.")
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @Operation(summary = "특정 작가의 예약 가능한 프로그램 조회(구현 완료)", description = "특정 작가의 예약 가능한 프로그램을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR002", description = "성공적으로 예약 가능한 프로그램을 조회했습니다.")
    })
    @GetMapping("/photographer/program")
    public ResultResponse<PhotographerProgramListResponse> getPhotographerProgram(
        @RequestParam("photographerId")
        Long photographerId
    ) {
        PhotographerProgramListResponse photographerProgramListResponse = programService.getPhotographerProgram(
            photographerId);
        return ResultResponse.of(ReservationResultCode.RESERVATION_PROGRAM_LOOK_UP,
            photographerProgramListResponse);
    }
}
