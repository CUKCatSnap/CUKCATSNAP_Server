package net.catsnap.domain.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.LoginMember;
import net.catsnap.domain.social.dto.SocialResponse;
import net.catsnap.domain.social.service.PhotographerSubscribeService;
import net.catsnap.domain.user.photographer.dto.PhotographerResponse;
import net.catsnap.global.result.PagedData;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "구독,차단 관련 API", description = "작가 구독, 장소 구독, 작가 차단에 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/social")
public class SocialController {

    private final PhotographerSubscribeService photographerSubscribeService;

    @Operation(summary = "구독한 작가 목록 또는 차단한 작가 목록 조회", description = "구독과 차단한 작가를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 So000", description = "성공적으로 구독 목록을 조회했습니다."),
    })
    @GetMapping("/{type}/photographer")
    public ResultResponse<PagedData<PhotographerResponse.PhotographerTinyInformationList>> getSubscribePhotographerList(
        @Parameter(description = "구독한 작가를 조회 : subscribe , 차단한 작가 조회: block")
        @PathVariable
        String type,
        @Parameter(hidden = true)
        Pageable pageable
    ) {
        return null;
    }

    @Operation(summary = "구독한 장소 목록 조회", description = "구독한 장소의 목록을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 So000", description = "성공적으로 구독 목록을 조회했습니다."),
    })
    @GetMapping("/subscribe/place")
    public ResultResponse<PagedData<SocialResponse.subscribePlaceList>> getSubscribePlaceList(
        @Parameter(hidden = true)
        Pageable pageable
    ) {
        return null;
    }

    @Operation(summary = "특정 작가를 구독하거나 구독 취소하는 API(구현 완료)", description = "특정 작가를 구독하거나 구독을 취소하는 API입니다. 이미 구독중이면 구독을 취소하고, 구독중이 아니면 구독합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SC001", description = "성공적으로 데이터를 생성했습니다."),
    })
    @LoginMember
    @PostMapping("/subscribe/photographer/{photographerId}")
    public ResponseEntity<ResultResponse<Void>> photographerSubscribeToggle(
        @UserId
        Long memberId,
        @Parameter(description = "구독을 토글할 작가의 id")
        @PathVariable
        Long photographerId
    ) {
        photographerSubscribeService.toggleSubscribePhotographer(photographerId, memberId);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE);
    }

    @Operation(summary = "특정 작가를 차단하거나 차단 취소하는 API", description = "특정 작가를 차단하거나 차단을 취소하는 API입니다. 이미 차단중이면 차단을 취소하고, 차단중이 아니면 차단합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 So002", description = "작가 차단(차단 취소)를 토글 했습니다."),
    })
    @PostMapping("block/photographer/{photographerId}")
    public ResultResponse<?> photographerBlockTogglePhotographer(
        @Parameter(description = "차단을 토글할 작가의 id")
        @PathVariable
        Long photographerId
    ) {
        return null;
    }

    @Operation(summary = "특정 장소를 구독하는 API", description = "특정 장소를 구독할 수 있는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 So003", description = "장소 구독을 했습니다."),
    })
    @PostMapping("/subscribe/place")
    public ResultResponse<?> placeSubscribe(
        @Parameter(description = "구독할 장소")
        @RequestParam("keyword")
        String keyword
    ) {
        return null;
    }

    @Operation(summary = "특정 장소의 구독을 취소하는 API", description = "특정 장소의 구독을 취소할 수 있는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 So004", description = "장소 구독을 취소했습니다."),
    })
    @DeleteMapping("/subscribe/place")
    public ResultResponse<?> placeSubscribeCancel(
        @Parameter(description = "구독을 취소할 장소의 Id")
        @RequestParam("subscribePlaceId")
        Long subscribePlaceId
    ) {
        return null;
    }
}
