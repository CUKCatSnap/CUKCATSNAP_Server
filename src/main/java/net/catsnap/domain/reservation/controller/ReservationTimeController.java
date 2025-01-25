package net.catsnap.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginPhotographer;
import net.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeListResponse;
import net.catsnap.domain.reservation.dto.photographer.request.ReservationTimeFormatRequest;
import net.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatIdResponse;
import net.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatListResponse;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.service.ReservationTimeService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.ReservationResultCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "작가 예약 시간 형식 관련 API",
    description = "작가가 예약 시간 형식을 관리할 수 있는 API입니다. 또한 사용자가 작가의 예약 시간 형식을 조회할 수 있습니다.")
@RestController
@RequestMapping("/reservation/photographer")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @Operation(summary = "특정 작가의 특정 날짜에 예약 가능한 시간 목록 조회(구현 완료)",
        description = "특정 작가의 특정 날짜에 예약 가능한 시간을 조회하는 API입니다. 특정 작가에게 예약을 할 때 조회되는 API입니다." +
            "만약 예약 가능한 시간이 없다면 photographerAvailableReservationTimeList는 빈 배열이 됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR001", description = "성공적으로 예약 가능한 시간을 조회했습니다.")
    })
    @GetMapping("/time")
    @AnyUser
    public ResponseEntity<ResultResponse<PhotographerAvailableReservationTimeListResponse>> getPhotographerAvailableReservationTimeList(
        @RequestParam("photographerId")
        Long photographerId,
        @Parameter(description = "예약을 하고 싶은 날", example = "yyyy-MM-dd")
        @RequestParam("date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date
    ) {
        PhotographerAvailableReservationTimeListResponse photographerAvailableReservationTimeListResponse = reservationTimeService.getAvailableReservationTime(
            date, photographerId);
        return ResultResponse.of(ReservationResultCode.RESERVATION_AVAILABLE_TIME_LOOK_UP,
            photographerAvailableReservationTimeListResponse);
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
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationTimeFormatIdResponse>> registerTimeFormat(
        @Parameter(description = "예약 시간 형식", required = true)
        @RequestBody
        ReservationTimeFormatRequest reservationTimeFormatRequest,
        @Parameter(description = "예약 시간 형식의 이름", required = false)
        @RequestParam(name = "timeFormatId", required = false)
        String timeFormatId
    ) {
        ReservationTimeFormatIdResponse reservationTimeFormatIdResponse = reservationTimeService.createReservationTimeFormat(
            reservationTimeFormatRequest, timeFormatId);
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT,
            reservationTimeFormatIdResponse);
    }

    @Operation(summary = "작가가 자신의 예약 시간 형식을 조회(구현 완료)",
        description = "작가가 자신의 예약 시간 형식을 조회하는 API입니다." +
            "예약 시간 형식은 Nosql에 저장되기 때문에 Id가 String 타입입니다."
    )
    @ApiResponses(
        @ApiResponse(responseCode = "200 SR012", description = "성공적으로 예약 시간 형식을 조회했습니다.")
    )
    @GetMapping("/my/timeformat")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationTimeFormatListResponse>> getTimeFormat() {
        ReservationTimeFormatListResponse reservationTimeFormatList = reservationTimeService.getMyReservationTimeFormatList();
        return ResultResponse.of(ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_LOOK_UP,
            reservationTimeFormatList);
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
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationResultCode>> deleteTimeFormat(
        @Parameter(description = "삭제하고자 하는 예약 시간 형식의 id", required = true)
        @RequestParam("timeFormatId")
        String timeFormatId
    ) {
        reservationTimeService.deleteReservationTimeFormat(timeFormatId);
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
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationResultCode>> mappingTimeFormatToWeekdays(
        @Parameter(description = "예약 형식을 만들고자 하는 요일" +
            "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY 중 1개의 값",
            required = true)
        @RequestParam("weekday")
        Weekday weekday,
        @Parameter(description = "등록하고자 하는 예약 시간 형식의 id", required = true)
        @RequestParam("timeFormatId")
        String timeFormatId
    ) {
        reservationTimeService.mappingWeekdayToReservationTimeFormat(timeFormatId, weekday);
        return ResultResponse.of(
            ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_MAPPING_WEEKDAY);
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
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ReservationResultCode>> unmappingTimeFormatToWeekdays(
        @Parameter(description = "시간 형식을 삭제하고자 하는 요일" +
            "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY 중 1개의 값",
            required = true)
        @RequestParam("weekday")
        Weekday weekday
    ) {
        reservationTimeService.unmappingWeekdayToReservationTimeFormatByWeekday(weekday);
        return ResultResponse.of(
            ReservationResultCode.PHOTOGRAPHER_RESERVATION_TIME_FORMAT_UNMAPPING_WEEKDAY);
    }
}
