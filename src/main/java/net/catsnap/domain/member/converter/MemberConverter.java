package net.catsnap.domain.member.converter;

import net.catsnap.domain.member.dto.MemberRequest;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.entity.SnsType;
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
}
