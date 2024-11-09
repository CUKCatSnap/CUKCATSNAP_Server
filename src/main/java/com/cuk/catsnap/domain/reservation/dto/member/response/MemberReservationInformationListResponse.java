package com.cuk.catsnap.domain.reservation.dto.member.response;

import java.util.List;

public record MemberReservationInformationListResponse(
    List<MemberReservationInformationResponse> memberReservationInformationResponseList
) {
    
    public static MemberReservationInformationListResponse from(
        List<MemberReservationInformationResponse> memberReservationInformationResponseList) {
        return new MemberReservationInformationListResponse(
            memberReservationInformationResponseList);
    }
}
