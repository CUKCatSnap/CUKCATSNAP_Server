package com.cuk.catsnap.domain.member.converter;

import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.member.dto.MemberResponse;
import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.entity.SnsType;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public Member memberRequestMemberSignUpToMember(MemberRequest.MemberSignUp memberSignUp,
        String hashedPassword) {
        return Member.builder()
            .identifier(memberSignUp.getIdentifier())
            .password(hashedPassword)
            .birthday(memberSignUp.getBirthday())
            .nickname(memberSignUp.getNickname())
            .phoneNumber(memberSignUp.getPhoneNumber())
            .snstype(SnsType.CATSNAP)
            .build();
    }

    public MemberResponse.MemberTinyInformation toMemberTinyInformation(Member member) {
        return MemberResponse.MemberTinyInformation.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .profilePhotoUrl(member.getProfilePhotoUrl())
            .build();
    }
}
