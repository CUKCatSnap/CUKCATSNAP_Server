package net.catsnap.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.LoginUser;
import net.catsnap.domain.notification.dto.response.NotificationListResponse;
import net.catsnap.domain.notification.service.NotificationReadService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알림 관련 API", description = "알림을 조회할 수 있는 API입니다.")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationReadService notificationReadService;

    @Operation(summary = "최근(30일 이내) 알림을 조회하는 API(구현 완료)", description = "최근(30일 이내) 알림을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다."),
    })
    @LoginUser
    @GetMapping
    public ResponseEntity<ResultResponse<SlicedData<NotificationListResponse>>> getNotification(
        @RequestParam
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @UserId
        Long userId
    ) {
        SlicedData<NotificationListResponse> notificationListResponseSlicedData
            = notificationReadService.getRecentNotification(userId, pageable,
            LocalDateTime.now().minusDays(30));

        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP,
            notificationListResponseSlicedData);
    }

    @Operation(summary = "오래된(30일 이후) 알림을 조회하는 API(구현 완료)", description = "오래된(30일 이후) 알림을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다."),
    })
    @LoginUser
    @GetMapping("/old")
    public ResponseEntity<ResultResponse<SlicedData<NotificationListResponse>>> getOldNotification(
        @RequestParam
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @UserId
        Long userId
    ) {
        SlicedData<NotificationListResponse> notificationListResponseSlicedData
            = notificationReadService.getOldNotification(userId, pageable,
            LocalDateTime.now().minusDays(30));
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP,
            notificationListResponseSlicedData);
    }

    @Operation(summary = "읽지 않은 알림의 수를 조회하는 API", description = "읽지 않은 알림의 수를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다."),
    })
    @LoginUser
    @GetMapping("/unread-count")
    public ResponseEntity<ResultResponse<?>> getUnreadNotificationCount(
        @UserId
        Long userId
    ) {
        return null;
    }


    @Operation(summary = "알림을 삭제하는 API", description = "알림을 삭제하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SN002", description = "성공적으로 알림을 삭제했습니다.")
    })
    @DeleteMapping("/{notificationId}")
    public ResultResponse<?> deleteNotification(
        @PathVariable
        Long notificationId
    ) {
        return null;
    }

    @Operation(summary = "모든 알림을 삭제하는 API", description = "모든 알림을 삭제하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SN002", description = "성공적으로 모든 알림을 삭제했습니다.")
    })
    @DeleteMapping("/all")
    public ResultResponse<?> deleteAllNotification() {
        return null;
    }
}
