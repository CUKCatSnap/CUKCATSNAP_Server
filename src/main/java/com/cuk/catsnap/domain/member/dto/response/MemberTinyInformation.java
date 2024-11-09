package com.cuk.catsnap.domain.member.dto.response;

import com.cuk.catsnap.domain.member.entity.Member;

public record MemberTinyInformation(
    Long memberId, String nickname,
    String profilePhotoUrl
) {

    public static MemberTinyInformation from(Member member) {
        return new MemberTinyInformation(member.getId(), member.getNickname(),
            member.getProfilePhotoUrl());
    }

}
