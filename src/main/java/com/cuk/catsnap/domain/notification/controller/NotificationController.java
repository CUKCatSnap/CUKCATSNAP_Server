package com.cuk.catsnap.domain.notification.controller;

import com.cuk.catsnap.domain.notification.dto.NotificationResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림 관련 API", description = "알림을 조회할 수 있는 API입니다.")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Operation(summary = "알림을 조회하는 API", description = "알림을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SN000", description = "성공적으로 알림을 조회했습니다.")
    })
    @GetMapping
    public ResultResponse<PagedData<NotificationResponse.NotificationList>> getNotification(
            @RequestParam
            Pageable pageable
    ) {
        return null;
    }

    @Operation(summary = "알림을 읽음 처리하는 API", description = "알림을 읽음 처리하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SN001", description = "성공적으로 알림을 읽음 처리했습니다.")
    })
    @PatchMapping("/{notificationId}")
    public ResultResponse<?> patchNotificationRead(
            @PathVariable
            Long notificationId
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
