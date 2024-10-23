package com.cuk.catsnap.domain.reservation.controller;

import com.cuk.catsnap.domain.reservation.converter.ReservationConverter;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.service.ReservationService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.ReservationResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "작가 예약 관련 API", description = "작가가 예약을 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/reservation/photographer")
@RequiredArgsConstructor
public class PhotographerReservationController {

    private final ReservationService reservationService;
    private final ReservationConverter reservationConverter;

    @Operation(summary = "작가의 특정 월의 예약 유무를 일별로 조회", description = "작가 자신으로 예약된 예약 목록을 월별로 조회하는 API입니다. 예) 2024년 9월에 예약은 ? -> 2024년 9월 7일, 2024년 9월 13일")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/month")
    public ResultResponse<ReservationResponse.MonthReservationCheckList> getMyMonthReservationCheck(
            @Parameter(description = "조회하고 싶은 달", example = "yyyy-MM")
            @RequestParam("month")
            @DateTimeFormat(pattern = "yyyy-MM")
            LocalDate reservationMonth
    ){
        return null;
    }

    @Operation(summary = "작가의 특정 일의 예약 목록을 조회", description = "작가 자신으로 예약된 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/day")
    public ResultResponse<ReservationResponse.PhotographerReservationInformationList> getMyDayReservation(
            @Parameter(description = "조회하고 싶은 일", example = "yyyy-MM-dd")
            @RequestParam("day")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate reservationDay
    ){
        return null;
    }

    @Operation(summary = "작가가 자신의 예약 상태를 변경",
            description = "작가가 자신의 예약 상태를 변경하는 API입니다. " +
            "(PENDING -> APPROVED) (PENDING -> REJECTED) (APPROVED -> PHOTOGRAPHY_CANCELLED) 으로 변경 가능합니다." +
            " 변경하고자 하는 최종 상태만을 요청하면 됩니다."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "201 SR005", description = "성공적으로 예약 상태를 변경했습니다.")
    )
    @PostMapping("/my/status")
    public ResultResponse<?> changeReservationStatus(
            @Parameter(description = "예약 id", required = true)
            @RequestParam("reservationId")
            long reservationId,
            @Parameter(description = "변경하고자 하는 최종 상태", required = true)
            @RequestParam("status")
            ReservationState status
    ){
        return null;
    }

    @Operation(summary = "작가가 자신의 예약 시간 형식을 등록(구현 완료)",
            description = "작가가 자신의 예약 시간 형식을 등록하는 API입니다." +
                        "만약 수정을 원한다면, requestParameter로 timeFormatId 넘겨주어야 합니다." +
                        "timeFormat은 Nosql에 저장되기 때문에 Id가 String 타입입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR006", description = "성공적으로 예약 시간 형식을 등록했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @PostMapping("/my/timeformat")
    public ResultResponse<ReservationResponse.PhotographerReservationTimeFormatId> registerTimeFormat(
            @Parameter(description = "예약 시간 형식", required = true)
            @RequestBody
            ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat,
            @Parameter(description = "예약 시간 형식의 이름", required = false)
            @RequestParam("timeFormatId")
            String timeFormatId
    ){
        String reservationTimeFormatId = reservationService.createReservationTimeFormat(photographerReservationTimeFormat, timeFormatId.toString());
        ReservationResponse.PhotographerReservationTimeFormatId photographerReservationTimeFormatId =
                ReservationResponse.PhotographerReservationTimeFormatId.builder()
                .photographerReservationTimeFormatId(reservationTimeFormatId)
                .build();
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT,photographerReservationTimeFormatId);
    }

    @Operation(summary = "작가가 자신의 예약 시간 형식을 조회(구현 완료)",
            description = "작가가 자신의 예약 시간 형식을 조회하는 API입니다." +
                        "예약 시간 형식은 Nosql에 저장되기 때문에 Id가 String 타입입니다."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200 SR012", description = "성공적으로 예약 시간 형식을 조회했습니다.")
    )
    @GetMapping("/my/timeformat")
    public ResultResponse<ReservationResponse.PhotographerReservationTimeFormatList> getTimeFormat(){
        List<ReservationTimeFormat> reservationTimeFormats = reservationService.getMyReservationTimeFormatList();
        ReservationResponse.PhotographerReservationTimeFormatList photographerReservationTimeFormatList =
                reservationConverter.toPhotographerReservationTimeFormatList(reservationTimeFormats);

        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT, photographerReservationTimeFormatList);
    }

    @Operation(summary = "작가가 자신의 예약 시간 형식을 삭제(구현 완료)",
            description = "작가가 자신의 예약 시간 형식을 삭제하는 API입니다." +
                        "만약 해당 시간 형식을 요일에 등록한 경우, 요일에 등록된 시간 형식도 삭제됩니다." +
                        "timeFormat은 Nosql에 저장되기 때문에 Id가 String 타입입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR007", description = "성공적으로 예약 시간 형식을 삭제했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    }
    )
    @DeleteMapping("/my/timeformat")
    public ResultResponse<?> deleteTimeFormat(
            @Parameter(description = "삭제하고자 하는 예약 시간 형식의 id", required = true)
            @RequestParam("timeFormatId")
            String timeFormatId
    ){
        reservationService.deleteReservationTimeFormat(timeFormatId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_DELETE);
    }

    @Operation(
            summary = "작가가 요일에 자신의 예약 시간 형식을 등록(구현 완료)",
            description = "작가가 요일에 자신의 예약 시간 형식을 등록하는  API입니다." +
            "/photographer/timeformat API를 통해 등록한 시간 형식을 요일에 등록합니다." +
            "만약 기존에 등록한 시간 형식이 있다면 덮어씌웁니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201 SR008", description = "성공적으로 예약 시간 형식을 특정 요일에 등록했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @PostMapping("/my/weekday/timeformat")
    public ResultResponse<?> registerProgram(
            @Parameter(description = "예약 형식을 만들고자 하는 요일" +
                    "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY 중 1개의 값",
                    required = true)
            @RequestParam("weekday")
            Weekday weekday,
            @Parameter(description = "등록하고자 하는 예약 시간 형식의 id", required = true)
            @RequestParam("timeFormatId")
            String timeFormatId
    ){
        reservationService.mappingWeekdayToReservationTimeFormat(timeFormatId, weekday);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_MAPPING_WEEKDAY);
    }

    @Operation(
            summary = "작가가 요일에 자신의 예약 시간 형식을 삭제(구현 완료)",
            description = "작가가 요일에 자신의 예약 시간 형식을 삭제하는 API입니다." +
            "만약 해당 요일에 등록된 시간 형식이 없다면, 아무런 동작도 하지 않습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR009", description = "성공적으로 특정 요일의 예약 시간 형식의 등록을 삭제했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @DeleteMapping("/my/weekday/timeformat")
    public ResultResponse<?> deleteProgram(
            @Parameter(description = "시간 형식을 삭제하고자 하는 요일" +
                    "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY 중 1개의 값",
                    required = true)
            @RequestParam("weekday")
            Weekday weekday
    ){
        reservationService.unmappingWeekdayToReservationTimeFormatByWeekday(weekday);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_UNMAPPING_WEEKDAY);
    }

    @Operation (
            summary = "작가가 자신의 예약 프로그램을 등록하거 수정(구현 완료)",
            description = "작가가 자신의 예약 프로그램을 등록하거 수정하는 API입니다." +
                    "만약 수정을 원한다면, requestParameter로 programId 넘겨주어야 합니다." +
                    "내부적으로 프로그램 수정은 새로운 프로그램을 등록하기 때문에 프로그램 Id가 바뀜을 유의해야 합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201 SR010", description = "성공적으로가 예약 프로그램을 등록(수정)했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @PostMapping("/my/program")
    public ResultResponse<ReservationResponse.PhotographerProgramId> postProgram(
            @Parameter(description = "예약 프로그램", required = true)
            @RequestBody
            ReservationRequest.PhotographerProgram photographerProgram,
            @Parameter(description = "예약 프로그램의 id, 수정을 할 때만 입력", required = false)
            @RequestParam("programId")
            Long programId
    ){
        Long photographerProgramId =  reservationService.createProgram(photographerProgram, programId);
        ReservationResponse.PhotographerProgramId photographerProgramIdResponse = ReservationResponse.PhotographerProgramId.builder()
                .photographerProgramId(photographerProgramId)
                .build();
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_POST_PROGRAM, photographerProgramIdResponse);
    }

    @Operation(
            summary = "작가가 자신의 예약 프로그램을 조회(구현 완료)",
            description = "작가가 자신의 예약 프로그램을 조회하는 API입니다."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200 SR013", description = "성공적으로가 예약 프로그램을 조회했습니다.")
    )
    @GetMapping("/my/program")
    public ResultResponse<ReservationResponse.PhotographerProgramList> getProgram() {
        List<Program> programList = reservationService.getMyProgramList();
        ReservationResponse.PhotographerProgramList photographerProgramList = reservationConverter.toPhotographerProgramList(programList);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_LOOK_UP_PROGRAM, photographerProgramList);
    }

    @Operation(
            summary = "작가가 자신의 예약 프로그램을 삭제(구현 완료)",
            description = "작가가 자신의 예약 프로그램을 삭제하는 API입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR011", description = "성공적으로가 예약 프로그램을 삭제했습니다."),
            @ApiResponse(responseCode = "404 SO000", description = "해당 게시물의 소유권을 찾을 수 없습니다.")
    })
    @DeleteMapping("/my/program")
    public ResultResponse<?> deleteProgram(
            @Parameter(description = "삭제하고자 하는 예약 프로그램의 id", required = true)
            @RequestParam("programId")
            Long programId
    ){
        reservationService.softDeleteProgram(programId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_DELETE_PROGRAM);
    }
}
