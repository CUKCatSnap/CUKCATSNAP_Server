package net.catsnap.domain.user.member.dto.response;

import net.catsnap.domain.user.member.entity.Member;

public record MemberTinyInformationResponse(
    Long memberId, String nickname,
    String profilePhotoUrl
) {

    public static MemberTinyInformationResponse from(Member member) {
        return new MemberTinyInformationResponse(member.getId(), member.getNickname(),
            member.getProfilePhotoUrl());
    }

}
