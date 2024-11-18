package com.cuk.catsnap.domain.reservation.controller;

import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.member.response.MemberReservationInformationListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerReservationGuidanceResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.ReservationBookResultResponse;
import com.cuk.catsnap.domain.reservation.entity.ReservationQueryType;
import com.cuk.catsnap.domain.reservation.service.MemberReservationService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.SlicedData;
import com.cuk.catsnap.global.result.code.ReservationResultCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 예약 관련 API", description = "새로운 예약을 생성하거나 자신의 예약을 조회하는 API입니다.")
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class MemberReservationController {

    private final MemberReservationService memberReservationService;

    @Operation(summary = "예약을 조회하는 API(구현 완료)", description = "예약을 조회하는 API입니다. 쿼리 파라미터 type으로 적절한 유형의 예약을 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다."),
    })
    @GetMapping("/member/my")
    public ResponseEntity<ResultResponse<SlicedData<MemberReservationInformationListResponse>>> getMyReservation(
        @Parameter(description = "ALL : 내 모든 예약(정렬 : 최근 예약한 시간 느릴수록 먼저옴) UPCOMING : 미래에 시작하는 예약(정렬 : 미래 예약 중 현재와 가까운 것이 먼저옴. 예약의 상태가 PENDING, APPROVED 인 것만 보여줌) ")
        @RequestParam("type")
        ReservationQueryType reservationQueryType,
        Pageable pageable) {
        SlicedData<MemberReservationInformationListResponse> memberReservationInformationList = memberReservationService.getMyReservation(
            reservationQueryType, pageable);

        return ResultResponse.of(ReservationResultCode.RESERVATION_LOOK_UP,
            memberReservationInformationList);
    }

    @Operation(summary = "특정 월의 예약 유무를 일별로 조회(구현 완료)", description = "특정 월의 예약 유무를 일별로 조회하는 API입니다. 예) 2024년 9월에 예약은 ? -> 2024년 9월 7일, 2024년 9월 13일")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/member/my/month")
    public ResponseEntity<ResultResponse<MonthReservationCheckListResponse>> getMyMonthReservationCheck(
        @Parameter(description = "조회하고 싶은 달", example = "yyyy-MM")
        @RequestParam("month")
        @DateTimeFormat(pattern = "yyyy-MM")
        YearMonth reservationMonth
    ) {
        MonthReservationCheckListResponse monthReservationCheckListResponse = memberReservationService.getReservationListByMonth(
            reservationMonth.atDay(1));
        return ResultResponse.of(ReservationResultCode.RESERVATION_LOOK_UP,
            monthReservationCheckListResponse);
    }

    @Operation(summary = "특정 일의 예약 목록을 조회(구현 완료)", description = "특정 일의 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/member/my/day")
    public ResponseEntity<ResultResponse<MemberReservationInformationListResponse>> getMyDayReservation(
        @Parameter(description = "조회하고 싶은 일", example = "yyyy-MM-dd")
        @RequestParam("day")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reservationDay
    ) {
        MemberReservationInformationListResponse memberReservationInformationListResponse = memberReservationService.getReservationDetailListByDay(
            reservationDay);
        return ResultResponse.of(ReservationResultCode.RESERVATION_LOOK_UP,
            memberReservationInformationListResponse);
    }

    @Operation(summary = "특정 작가의 예약 시 주의사항과 예약 가능한 장소 조회(구현 완료)", description = "특정 작가의 예약 시 주의사항과 예약 가능한 장소 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR003", description = "성공적으로 예약 주의사항과 예약 가능한 장소를 조회했습니다."),
        @ApiResponse(responseCode = "404 EO000", description = "해당 게시물의 소유권을 찾을 수 없습니다."),
    })
    @GetMapping("/photographer/guidance")
    public ResponseEntity<ResultResponse<PhotographerReservationGuidanceResponse>> getPhotographerReservationGuidance(
        @RequestParam("photographerId")
        Long photographerId
    ) {
        PhotographerReservationGuidanceResponse photographerReservationGuidance = memberReservationService.getPhotographerReservationGuidance(
            photographerId);
        return ResultResponse.of(ReservationResultCode.RESERVATION_GUIDANCE_LOOK_UP,
            photographerReservationGuidance);
    }

    @Operation(summary = "새로운 예약을 생성하는 API(구현 완료)", description = "새로운 예약을 만드는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SR004", description = "예약이 성공적으로 완료되었습니다."),
        @ApiResponse(responseCode = "404 EP001", description = "존재하지 않는 프로그램입니다"),
        @ApiResponse(responseCode = "404 EP002", description = "삭제된 프로그램입니다"),
        @ApiResponse(responseCode = "400 EP003", description = "해당 요일에 요청하는 시작시간이 존재하지 않습니다."),
        @ApiResponse(responseCode = "400 EP004", description = "해당 시간대는 예약 중복으로 인해 예약이 불가능합니다."),
        @ApiResponse(responseCode = "400 EP005", description = "신규 예약의 시작시간은 현재보다 과거일 수 없습니다.")

    })
    @PostMapping("/member/book")
    public ResponseEntity<ResultResponse<ReservationBookResultResponse>> postBookReservation(
        @Parameter(description = "새로운 예약 형식")
        @RequestBody
        MemberReservationRequest memberReservationRequest
    ) {
        ReservationBookResultResponse reservationBookResultResponse = memberReservationService.createReservation(
            memberReservationRequest);
        return ResultResponse.of(ReservationResultCode.RESERVATION_BOOK_COMPLETE,
            reservationBookResultResponse);
    }
}
