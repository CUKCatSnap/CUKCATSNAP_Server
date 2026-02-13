package net.catsnap.domain.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.interceptor.LoginMember;
import net.catsnap.domain.social.dto.response.AddressListResponse;
import net.catsnap.domain.social.service.AddressService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "구독할 장소 설정 API", description = "구독할 장소를 설정하는 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "시도명을 조회하는 API(구현 완료)", description = "시도명(서울시, 경기도 등)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/city")
    public ResponseEntity<ResultResponse<AddressListResponse>> getCityLevel() {
        AddressListResponse addressListResponse = addressService.getCityLevel();
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, addressListResponse);
    }

    @Operation(summary = "시군구를 조회하는 API(구현 완료)", description = "특정 시나 도에 있는 시군구(경기도 부천시)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/district")
    public ResponseEntity<ResultResponse<SlicedData<AddressListResponse>>> getDistrictLevel(
        @RequestParam(value = "cityId") Long cityId,
        @PageableDefault(size = 10, sort = {"districtName" }, direction = Direction.ASC)
        Pageable pageable
    ) {
        SlicedData<AddressListResponse> addressListResponse = addressService.getDistrictLevel(
            cityId,
            pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, addressListResponse);
    }

    @Operation(summary = "동을 조회하는 API(구현 완료)", description = "특정 시군구에 있는 동(경기도 부천시 역곡동)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/town")
    public ResponseEntity<ResultResponse<SlicedData<AddressListResponse>>> getTownLevel(
        @RequestParam(value = "districtId") Long districtId,
        @PageableDefault(size = 10, sort = {"townName" }, direction = Direction.ASC)
        Pageable pageable
    ) {
        SlicedData<AddressListResponse> addressListResponse = addressService.getTownLevel(
            districtId,
            pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, addressListResponse);
    }
}
