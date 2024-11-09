package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.global.jsonformat.deserialize.HoursMinutesListSerializer;
import com.cuk.catsnap.global.jsonformat.serializer.HoursMinutesListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationResponse {

    @Getter
    @Builder
    @Schema(description = "예약의 위치를 나타내는 필드", nullable = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {

        private Double latitude;
        private Double longitude;
        private String locationName;
    }

    @Getter
    @Builder
    public static class PhotographerReservationGuidance {

        @Schema(description = "작가가 설정한 예약 가능한 장소입니다. 주소 형태가 아니라 문자열 형태입니다.")
        private String photographerLocation;
        @Schema(description = "작가가 설정한 예약전 주의사항입니다.")
        private String photographerNotification;
    }

    @Getter
    @Builder
    public static class ReservationBookResult {

        private Long reservationId;
        @Schema(description = "예약의 상태를 의미합니다. 작가의 설정에 따라 바로 완료 될 수도 있고, 완료 대기일 수도 있습니다. (PENDING, APPROVED)")
        private ReservationState reservationState;
    }

    @Getter
    @Builder
    public static class PhotographerReservationTimeFormatId {

        private String photographerReservationTimeFormatId;
    }

    @Getter
    @Builder
    public static class PhotographerProgramId {

        private Long photographerProgramId;
    }

    @Getter
    @Builder
    public static class PhotographerReservationTimeFormatList {

        private List<PhotographerReservationTimeFormat> reservationTimeFormatList;
    }

    @Getter
    @Builder
    public static class PhotographerReservationTimeFormat {

        private String reservationTimeFormatId;
        private String formatName;
        @Schema(description = "예약의 시작 시간", example = "HH:mm", type = "string")
        @JsonSerialize(using = HoursMinutesListSerializer.class)
        @JsonDeserialize(using = HoursMinutesListDeserializer.class)
        private List<LocalTime> startTimeList;
    }
}
