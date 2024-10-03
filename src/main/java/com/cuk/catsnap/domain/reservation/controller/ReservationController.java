package com.cuk.catsnap.domain.reservation.controller;

import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "예약 관련 API", description = "사용자가 예약한 예약 조회, 새로운 예약하기, 특정 작가의 예약을 조회할 수 있는 API입니다.")
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Operation(summary = "예약을 조회하는 API", description = "예약을 조회하는 API입니다. 쿼리 파라미터 type으로 적절한 유형의 예약을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다."),
            @ApiResponse(responseCode = "400 SR000", description = "예약 조회 실패", content = @Content(schema = @Schema(implementation = ResultResponse.class))),
    })
    @GetMapping("/my")
    public ResultResponse<PagedData<ReservationResponse.MyReservationList>> getMyReservation(
            @Parameter(description="all : 내 모든 예약(정렬 : 최근 예약한 시간 느릴수록 먼저옴) upcoming : 미래에 시작하는 예약(정렬 : 미래 예약 중 현재와 가까운 것이 먼저옴) ")
            @RequestParam("type")
            String reservationQuery,
            Pageable pageable){
        return null;
    }

    @Operation(summary = "특정 월의 예약 유무를 일별로 조회", description = "특정 월의 예약 유무를 일별로 조회하는 API입니다. 예) 2024년 9월에 예약은 ? -> 2024년 9월 7일, 2024년 9월 13일")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/month")
    public ResultResponse<ReservationResponse.MonthReservationCheckList> getMyMonthReservationCheck(
            @Parameter(description = "조회하고 싶은 달")
            @RequestParam("month")
            @DateTimeFormat(pattern = "yyyy-MM")
            LocalDate reservationMonth
    ){
        return null;
    }

    @Operation(summary = "특정 일의 예약 목록을 조회", description = "특정 일의 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/day")
    public ResultResponse<ReservationResponse.DayReservationCheckList> getMyDayReservation(
            @RequestParam("day")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate reservationDay
    ){
        return null;
    }

}
