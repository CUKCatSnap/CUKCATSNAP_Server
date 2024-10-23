package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.member.dto.MemberResponse;
import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        private PhotographerResponse.PhotographerFullyInformation photographerTinyInformation;
        private Location location;
        private Time time;
        private ReservedProgram reservedProgram;
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
    public static class ReservedProgram {
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
    public static class MemberReservationInformationList {
        List<MemberReservationInformation> memberReservationInformationList;
    }

    @Getter
    @Builder
    public static class MemberReservationInformation {
        private Long reservationId;
        private PhotographerResponse.PhotographerTinyInformation photographerTinyInformation;
        private Location location;
        private String locationName;
        private Time time;
        private ReservedProgram reservedProgram;
        @Schema(description = "예약의 상태를 나타냅니다.", nullable = false, example = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED 중 한개의 값")
        private ReservationState state;
    }

    @Getter
    @Builder
    public static class PhotographerReservationInformationList {
        List<PhotographerReservationInformation> photographerReservationInformationList;
    }

    @Getter
    @Builder
    public static class PhotographerReservationInformation {
        private Long reservationId;
        private MemberResponse.MemberTinyInformation memberTinyInformation;
        private Location location;
        private String locationName;
        private Time time;
        private ReservedProgram reservedProgram;
        @Schema(description = "예약의 상태를 나타냅니다.", nullable = false, example = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED 중 한개의 값")
        private ReservationState state;
    }

    @Getter
    @Builder
    public static class PhotographerAvailableReservationTimeList {
        private List<PhotographerAvailableReservationTime> photographerAvailableReservationTimeList;
    }

    @Getter
    @Builder
    public static class PhotographerAvailableReservationTime {
        private Time time;
        @Schema(description = "true이면 해당 시간에 예약이 가능함")
        private Boolean isAvailableReservation;
    }

    @Getter
    @Builder
    public static class PhotographerProgramList {
        private List<PhotographerProgram> photographerProgramList;
    }

    @Getter
    @Builder
    public static class PhotographerProgram{
        private Long programId;
        private String title;
        private String content;
        private Long price;
        private Long durationMinutes;
    }

    @Getter
    @Builder
    public static class PhotographerReservationGuidance{
        @Schema(description = "작가가 설정한 예약 가능한 장소입니다. 주소 형태가 아니라 문자열 형태입니다.")
        private String availablePlace;
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
        private List<LocalTime> startTimeList;
    }
}
