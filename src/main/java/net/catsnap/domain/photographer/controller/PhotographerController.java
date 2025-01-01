package net.catsnap.domain.photographer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.photographer.converter.PhotographerConverter;
import net.catsnap.domain.photographer.document.PhotographerReservationLocation;
import net.catsnap.domain.photographer.document.PhotographerReservationNotice;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import net.catsnap.domain.photographer.dto.PhotographerRequest;
import net.catsnap.domain.photographer.dto.PhotographerResponse;
import net.catsnap.domain.photographer.service.PhotographerService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.PhotographerResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사진작가 관련 API", description = "사진작가를 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/photographer")
@RequiredArgsConstructor
public class PhotographerController {

    private final PhotographerService photographerService;
    private final PhotographerConverter photographerConverter;
    
    @Operation(summary = "작가가 자신의 예약 설정을 조회하는 API(구현 완료)", description = "작가가 자신의 예약 설정을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SP001", description = "사진작가 자신의 환경설정 조회 성공")
    })
    @GetMapping("/my/setting")
    public ResponseEntity<ResultResponse<PhotographerResponse.PhotographerSetting>> lookUpMySetting() {
        PhotographerSetting photographerSetting = photographerService.getPhotographerSetting();
        PhotographerResponse.PhotographerSetting photographerSettingDto = photographerConverter.toPhotographerSetting(
            photographerSetting);
        return ResultResponse.of(PhotographerResultCode.LOOK_UP_MY_SETTING, photographerSettingDto);
    }

    @Operation(summary = "작가가 자신의 예약 설정을 수정하는 API(구현 완료)", description = "작가가 자신의 예약 설정을 수정하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SP002", description = "사진작가 자신의 환경설정 변경 성공")
    })
    @PostMapping("/my/setting")
    public ResponseEntity<ResultResponse<PhotographerResultCode>> updateMySetting(
        @Parameter(description = "작가의 환경설정", required = true)
        @RequestBody
        PhotographerRequest.PhotographerSetting photographerSetting
    ) {
        photographerService.updatePhotographerSetting(photographerSetting);
        return ResultResponse.of(PhotographerResultCode.UPDATE_MY_SETTING);
    }

    @Operation(summary = "작가가 자신의 예약 전 알림을 조회하는 API(구현 완료)", description = "작가가 자신의 예약 전 알림을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SP003", description = "사진작가 자신의 예약 전 알림 조회 성공")
    })
    @GetMapping("/my/reservation/notice")
    public ResponseEntity<ResultResponse<PhotographerResponse.PhotographerReservationNotice>> lookUpMyReservationNotice(
    ) {
        PhotographerReservationNotice photographerReservationNotice = photographerService.getReservationNotice();
        PhotographerResponse.PhotographerReservationNotice photographerReservationNoticeDto = photographerConverter.toPhotographerReservationNotice(
            photographerReservationNotice);
        return ResultResponse.of(PhotographerResultCode.LOOK_UP_RESERVATION_NOTICE,
            photographerReservationNoticeDto);
    }

    @Operation(summary = "작가가 자신의 예약 전 알림을 수정하는 API(구현 완료)", description = "작가가 자신의 예약 전 알림을 수정하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SP004", description = "사진작가 자신의 예약 전 알림 변경 성공")
    })
    @PostMapping("/my/reservation/notice")
    public ResponseEntity<ResultResponse<PhotographerResultCode>> updateMyReservationNotice(
        @Parameter(description = "작가의 예약 전 알림", required = true)
        @RequestBody
        PhotographerRequest.PhotographerReservationNotice photographerReservationNotice
    ) {
        photographerService.updateReservationNotice(photographerReservationNotice);
        return ResultResponse.of(PhotographerResultCode.UPDATE_RESERVATION_NOTICE);
    }

    @Operation(summary = "작가가 자신이 예약을 받을 장소를 조회하는 API(구현 완료)", description = "작가가 자신이 예약을 받을 장소를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SP005", description = "사진작가 자신의 예약 전 알림 조회 성공")
    })
    @GetMapping("/my/reservation/location")
    public ResponseEntity<ResultResponse<PhotographerResponse.PhotographerReservationLocation>> lookUpMyReservationLocation(
    ) {
        PhotographerReservationLocation photographerReservationLocation = photographerService.getReservationLocation();
        PhotographerResponse.PhotographerReservationLocation photographerReservationLocationDto = photographerConverter.toPhotographerReservationLocation(
            photographerReservationLocation);
        return ResultResponse.of(PhotographerResultCode.LOOK_UP_RESERVATION_LOCATION,
            photographerReservationLocationDto);
    }

    @Operation(summary = "작가가 자신이 예약을 받을 장소를 수정하는 API(구현 완료)", description = "작가가 자신이 예약을 받을 장소를 수정하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SP006", description = "사진작가 자신의 예약 전 알림 변경 성공")
    })
    @PostMapping("/my/reservation/location")
    public ResponseEntity<ResultResponse<PhotographerResultCode>> updateMyReservationLocation(
        @Parameter(description = "작가의 예약 전 알림", required = true)
        @RequestBody
        PhotographerRequest.PhotographerReservationLocation photographerReservationLocation
    ) {
        photographerService.updateReservationLocation(photographerReservationLocation);
        return ResultResponse.of(PhotographerResultCode.UPDATE_RESERVATION_LOCATION);
    }
}
