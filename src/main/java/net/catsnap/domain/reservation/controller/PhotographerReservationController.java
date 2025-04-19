package net.catsnap.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.LoginPhotographer;
import net.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import net.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationListResponse;
import net.catsnap.domain.reservation.entity.ReservationState;
import net.catsnap.domain.reservation.service.PhotographerReservationService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.result.code.CommonResultCode;
import net.catsnap.global.result.code.ReservationResultCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "작가 예약 관련 API", description = "작가가 예약을 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/reservation/photographer")
@RequiredArgsConstructor
public class PhotographerReservationController {

    private final PhotographerReservationService photographerReservationService;

    @Operation(summary = "작가의 특정 월의 특정 일에 예약 유무를 일별로 조회(구현 완료)",
        description = "작가 자신으로 예약된 예약 목록을 월별로 조회하는 API입니다. " +
            "예) 2024년 9월에 예약이 있는 날은 ? -> 2024년 9월 7일, 2024년 9월 13일")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/month")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<MonthReservationCheckListResponse>> getMyMonthReservationCheck(
        @Parameter(description = "조회하고 싶은 달", example = "yyyy-MM")
        @RequestParam("month")
        YearMonth reservationMonth
    ) {
        MonthReservationCheckListResponse monthReservationCheckListResponse = photographerReservationService.getReservationListByMonth(
            reservationMonth.atDay(1));
        return ResultResponse.of(ReservationResultCode.RESERVATION_LOOK_UP,
            monthReservationCheckListResponse);
    }

    @Operation(summary = "작가의 특정 일의 예약 목록을 조회(구현 완료)",
        description = "작가 자신으로 예약된 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/my/day")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<PhotographerReservationInformationListResponse>> getMyDayReservation(
        @Parameter(description = "조회하고 싶은 일", example = "yyyy-MM-dd")
        @RequestParam("day")
        LocalDate reservationDay
    ) {
        PhotographerReservationInformationListResponse photographerReservationInformationListResponse = photographerReservationService.getReservationDetailListByDay(
            reservationDay);
        return ResultResponse.of(ReservationResultCode.RESERVATION_LOOK_UP,
            photographerReservationInformationListResponse);
    }

    @Operation(summary = "작가의 모든 예약을 조회합니다(구현 완료)",
        description = "작가 자신으로 예약된 모든 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @GetMapping("/my/all")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<SlicedData<PhotographerReservationInformationListResponse>>> getMyReservation(
        @UserId
        Long photographerId,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC)
        Pageable pageable
    ) {
        SlicedData<PhotographerReservationInformationListResponse> myReservation = photographerReservationService.getMyReservation(
            photographerId, pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, myReservation);
    }

    @Operation(summary = "작가가 자신의 예약 상태를 변경(구현 완료)",
        description = "작가가 자신의 예약 상태를 변경하는 API입니다. " +
            "(PENDING -> APPROVED) (PENDING -> REJECTED) (APPROVED -> PHOTOGRAPHY_CANCELLED) 으로 변경 가능합니다."
            +
            " 변경하고자 하는 최종 상태만을 요청하면 됩니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201 SR005", description = "성공적으로 예약 상태를 변경했습니다."),
        @ApiResponse(responseCode = "400 ER000", description = "해당 예약 상태에서 요청하신 예약상태로 변경할 수 없습니다.")
    })
    @PostMapping("/my/status")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationResultCode>> changeReservationStatus(
        @Parameter(description = "예약 id", required = true)
        @RequestParam("reservationId")
        long reservationId,
        @Parameter(description = "변경하고자 하는 최종 상태", required = true)
        @RequestParam("status")
        ReservationState status
    ) {
        photographerReservationService.changeReservationState(reservationId, status);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_STATE_CHANGE);
    }
}
