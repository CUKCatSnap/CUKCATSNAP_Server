package net.catsnap.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginPhotographer;
import net.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import net.catsnap.domain.reservation.dto.photographer.request.ProgramRequest;
import net.catsnap.domain.reservation.dto.photographer.response.photographerProgramIdResponse;
import net.catsnap.domain.reservation.service.ProgramService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.ReservationResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @AnyUser
    public ResponseEntity<ResultResponse<PhotographerProgramListResponse>> getPhotographerProgram(
        @RequestParam("photographerId")
        Long photographerId
    ) {
        PhotographerProgramListResponse photographerProgramListResponse = programService.getPhotographerProgram(
            photographerId);
        return ResultResponse.of(ReservationResultCode.RESERVATION_PROGRAM_LOOK_UP,
            photographerProgramListResponse);
    }

    @Operation(
        summary = "작가가 자신의 예약 프로그램을 등록하거 수정(구현 완료)",
        description = "작가가 자신의 예약 프로그램을 등록하거 수정하는 API입니다." +
            "만약 수정을 원한다면, requestParameter로 programId 넘겨주어야 합니다." +
            "내부적으로 프로그램 수정은 새로운 프로그램을 등록하기 때문에 프로그램 Id가 바뀜을 유의해야 합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201 SR010", description = "성공적으로가 예약 프로그램을 등록(수정)했습니다."),
        @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @PostMapping("/photographer/my/program")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<photographerProgramIdResponse>> postProgram(
        @Parameter(description = "예약 프로그램", required = true)
        @RequestBody
        ProgramRequest programRequest,
        @Parameter(description = "예약 프로그램의 id, 수정을 할 때만 입력", required = false)
        @RequestParam(name = "programId", required = false)
        Long programId,
        @UserId
        Long photographerId
    ) {
        photographerProgramIdResponse photographerProgramIdResponse = programService.createProgram(
            programRequest, programId, photographerId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_POST_PROGRAM,
            photographerProgramIdResponse);
    }

    @Operation(
        summary = "작가가 자신의 예약 프로그램을 조회(구현 완료)",
        description = "작가가 자신의 예약 프로그램을 조회하는 API입니다."
    )
    @ApiResponses(
        @ApiResponse(responseCode = "200 SR013", description = "성공적으로가 예약 프로그램을 조회했습니다.")
    )
    @GetMapping("/photographer/my/program")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<PhotographerProgramListResponse>> getProgram(
        @UserId
        Long photographerId
    ) {
        PhotographerProgramListResponse photographerProgramListResponse = programService.getMyProgramList(
            photographerId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_LOOK_UP_PROGRAM,
            photographerProgramListResponse);
    }

    @Operation(
        summary = "작가가 자신의 예약 프로그램을 삭제(구현 완료)",
        description = "작가가 자신의 예약 프로그램을 삭제하는 API입니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR011", description = "성공적으로가 예약 프로그램을 삭제했습니다."),
        @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @DeleteMapping("/photographer/my/program")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationResultCode>> deleteProgram(
        @Parameter(description = "삭제하고자 하는 예약 프로그램의 id", required = true)
        @RequestParam("programId")
        Long programId,
        @UserId
        Long photographerId
    ) {
        programService.softDeleteProgram(programId, photographerId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_DELETE_PROGRAM);
    }
}
