package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponse {


    @Getter
    @Builder
    public static class MyReservationList{
        @Schema(description = "현재 API에 맞는 상태의 예약만을 보여줍니다", nullable = true)
        private List<MyReservation> myReservationList;
    }

    @Getter
    @Builder
    public static class MyReservation{
        @Schema(description = "예약 id", nullable = false)
        private long reservationId;
        private PhotographerResponse.PhotographerTinyInformation photographerTinyInformation;
        private Location location;
        private Time time;
        private Program program;
        @Schema(description = "예약의 상태를 나타냅니다.", nullable = false, example = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED 중 한개의 값")
        private ReservationState state;
    }

    @Getter
    @Builder
    @Schema(description = "예약의 위치를 나타내는 필드", nullable = false)
    public static class Location{
        private double lat;
        private double lng;
        private String locationName;
    }

    @Getter
    @Builder
    @Schema(description = "예약의 시간을 나타내는 필드", nullable = false)
    public static class Time{
        @Schema(example = "yyyy-MM-dd HH:mm:ss", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startTime;
        @Schema(example = "yyyy-MM-dd HH:mm:ss", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endTime;
    }

    @Getter
    @Builder
    @Schema(description = "예약한 프로그램의 정보를 나타내는 필드", nullable = false)
    public static class Program{
        private String title;
        private String content;
        private long price;
    }

    @Getter
    @Builder
    public static class MonthReservationCheckList{
        List<MonthReservationCheck> monthReservationCheckList;
    }

    @Getter
    @Builder
    public static class MonthReservationCheck{
        @Schema(description = "질문한 달의 예약이 있는 날",example = "yyyy-MM-dd", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
        @Schema(description = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED")
        private ReservationState reservationState;
    }

    @Getter
    @Builder
    public static class DayReservationCheckList{
        List<DayReservation> dayReservationList;
    }

    @Getter
    @Builder
    public static class DayReservation{
        private Long reservationId;
        private Time time;
        private Long photographerId;
        private String photographerNickname;
    }
}
