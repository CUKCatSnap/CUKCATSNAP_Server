package net.catsnap.domain.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.awt.print.Pageable;
import net.catsnap.domain.auth.interceptor.LoginMember;
import net.catsnap.domain.social.dto.response.AddressListResponse;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰 알림 설정 API", description = "내가 구독한 장소에 대한 리뷰 알림을 설정하는 API입니다.")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Operation(summary = "시도명을 조회하는 API", description = "시도명(서울시, 경기도 등)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/city")
    public ResultResponse<AddressListResponse> getCityLevel() {
        return null;
    }

    @Operation(summary = "시군구를 조회하는 API", description = "특정 시나 도에 있는 시군구(경기도 부천시)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/district")
    public ResultResponse<SlicedData<AddressListResponse>> getDistrictLevel(
        @RequestParam(value = "cityId") Long cityId,
        @PageableDefault(size = 10, sort = {"districtName" }, direction = Direction.ASC)
        Pageable pageable
    ) {

        return null;
    }

    @Operation(summary = "동을 조회하는 API", description = "특정 시군구에 있는 동(경기도 부천시 역곡동)을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @LoginMember
    @GetMapping("/town")
    public ResultResponse<SlicedData<AddressListResponse>> getTownLevel(
        @RequestParam(value = "districtId") Long districtId,
        @PageableDefault(size = 10, sort = {"townName" }, direction = Direction.ASC)
        Pageable pageable
    ) {

        return null;
    }
}
