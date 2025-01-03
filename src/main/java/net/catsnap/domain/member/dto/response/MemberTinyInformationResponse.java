package net.catsnap.domain.member.dto.response;

import net.catsnap.domain.member.entity.Member;

public record MemberTinyInformationResponse(
    Long memberId, String nickname,
    String profilePhotoUrl
) {

    public static MemberTinyInformationResponse from(Member member) {
        return new MemberTinyInformationResponse(member.getId(), member.getNickname(),
            member.getProfilePhotoUrl());
    }

}
