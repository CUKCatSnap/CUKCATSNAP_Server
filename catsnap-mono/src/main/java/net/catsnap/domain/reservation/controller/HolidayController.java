package net.catsnap.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.reservation.dto.HolidayListResponse;
import net.catsnap.domain.reservation.service.HolidayService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.ReservationResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "휴일 관련 API", description = "휴일을 조회하거나 등록하는 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/holiday")
public class HolidayController {

    private final HolidayService holidayService;

    @Operation(summary = "공휴일 정보를 업데이트 하는 API입니다(관리자 전용)(구현완료)", description = "공휴일 정보를 업데이트 하는 API입니다(관리자 전용)")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR014", description = "성공적으로 휴일 정보를 업데이트했습니다."),
    })
    @PostMapping
    @AnyUser
    public ResponseEntity<ResultResponse<ReservationResultCode>> saveHolidays() {
        holidayService.saveHolidays();
        return ResultResponse.of(ReservationResultCode.HOLIDAY_UPDATE);
    }

    @Operation(summary = "특정 달의 공휴일 정보를 가져오는 API(구현완료)", description = "2년 내의 공휴일 정보만 가능합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SR015", description = "성공적으로 휴일 정보를 조회했습니다."),
    })
    @GetMapping
    @AnyUser
    public ResponseEntity<ResultResponse<HolidayListResponse>> getHoliday(
        @RequestParam("yearMonth")
        @Parameter(description = "공휴일 조회하고 싶은 달", example = "yyyy-MM")
        YearMonth yearMonth
    ) {
        HolidayListResponse holidayListResponse = holidayService.getHoliday(yearMonth);
        return ResultResponse.of(ReservationResultCode.HOLIDAY_LOOK_UP, holidayListResponse);
    }
}
