package com.cuk.catsnap.domain.photographer.controller;

import com.cuk.catsnap.domain.photographer.converter.PhotographerConverter;
import com.cuk.catsnap.domain.photographer.document.PhotographerReservationNotice;
import com.cuk.catsnap.domain.photographer.document.PhotographerSetting;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.photographer.service.PhotographerService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.PhotographerResultCode;
import com.cuk.catsnap.global.security.dto.SecurityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "작가 회원가입 API(구현 완료)", description = "작가가 회원가입을 할 수 있는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SP000", description = "사진작가 회원가입 성공."),
            @ApiResponse(responseCode = "409 EP000", description = "중복된 ID로 회원가입이 불가능 합니다.")
    })
    @PostMapping("/signup/catsnap")
    public ResultResponse<?> signUp(
            @Parameter(description = "회원가입 양식", required = true)
            @RequestBody
            PhotographerRequest.PhotographerSignUp photographerSignUp
    ) {
        photographerService.singUp(photographerSignUp);
        return ResultResponse.of(PhotographerResultCode.PHOTOGRAPHER_SIGN_UP);
    }

    /*
     *로그인 처리는 Spring Security의 필터로 처리하므로 해당 메서드는 필요하지 않습니다.
     *해당 컨트롤러는 API 명세만을 위한 것입니다.
     */
    @Operation(summary = "자체 서비스 API(구현 완료)", description = "작가 로그인은 자체 로그인만 지원합니다. 네이버 로그인 지원 안함.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SY000", description = "로그인 성공"),
            @ApiResponse(responseCode = "401 EY000", description = "로그인 실패 (아이디 또는 비밀번호가 일치하지 않음)"),
            @ApiResponse(responseCode = "400 EY001", description = "로그인 실패 (잘못된 로그인 API 요청 형식)"),
    })
    @PostMapping("/signin/catsnap")
    public ResultResponse<?> signIn(
            @Parameter(description = "로그인 양식", required = true)
            @RequestBody
            SecurityRequest.CatsnapSignInRequest photographerSignIn
    ) {
        return null;
    }

    @Operation(summary = "작가가 자신의 예약 설정을 조회하는 API(구현 완료)", description = "작가가 자신의 예약 설정을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SP001", description = "사진작가 자신의 환경설정 조회 성공")
    })
    @GetMapping("/my/setting")
    public ResultResponse<PhotographerResponse.PhotographerSetting> lookUpMySetting() {
        PhotographerSetting photographerSetting =  photographerService.getPhotographerSetting();
        PhotographerResponse.PhotographerSetting photographerSettingDto = photographerConverter.toPhotographerSetting(photographerSetting);
        return ResultResponse.of(PhotographerResultCode.LOOK_UP_MY_SETTING, photographerSettingDto);
    }

    @Operation(summary = "작가가 자신의 예약 설정을 수정하는 API(구현 완료)", description = "작가가 자신의 예약 설정을 수정하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SP002", description = "사진작가 자신의 환경설정 변경 성공")
    })
    @PostMapping("/my/setting")
    public ResultResponse<?> updateMySetting(
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
    public ResultResponse<PhotographerResponse.PhotographerReservationNotice> lookUpMyReservationNotice(
    ) {
        PhotographerReservationNotice photographerReservationNotice = photographerService.getReservationNotice();
        PhotographerResponse.PhotographerReservationNotice photographerReservationNoticeDto = photographerConverter.toPhotographerReservationNotice(photographerReservationNotice);
        return ResultResponse.of(PhotographerResultCode.LOOK_UP_RESERVATION_NOTICE, photographerReservationNoticeDto);
    }

    @Operation(summary = "작가가 자신의 예약 전 알림을 수정하는 API(구현 완료)", description = "작가가 자신의 예약 전 알림을 수정하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SP004", description = "사진작가 자신의 예약 전 알림 변경 성공")
    })
    @PostMapping("/my/reservation/notice")
    public ResultResponse<?> updateMyReservationNotice(
            @Parameter(description = "작가의 예약 전 알림", required = true)
            @RequestBody
            PhotographerRequest.PhotographerReservationNotice photographerReservationNotice
    ) {
        photographerService.updateReservationNotice(photographerReservationNotice);
        return ResultResponse.of(PhotographerResultCode.UPDATE_RESERVATION_NOTICE);
    }
}
