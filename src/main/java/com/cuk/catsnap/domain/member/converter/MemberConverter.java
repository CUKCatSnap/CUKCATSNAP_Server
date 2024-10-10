package com.cuk.catsnap.domain.member.converter;

import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.entity.SnsType;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public Member memberRequestMemberSignUpToMember(MemberRequest.MemberSignUp memberSignUp, String hashedPassword) {
        if(hashedPassword != null){
            return Member.builder()
                    .identifier(memberSignUp.getIdentifier())
                    .password(hashedPassword)
                    .birthday(memberSignUp.getBirthday())
                    .nickname(memberSignUp.getNickname())
                    .phoneNumber(memberSignUp.getPhoneNumber())
                    .snstype(SnsType.CATSNAP)
                    .build();
        } else{
            return Member.builder()
                    .identifier(memberSignUp.getIdentifier())
                    .password(memberSignUp.getPassword())
                    .birthday(memberSignUp.getBirthday())
                    .nickname(memberSignUp.getNickname())
                    .phoneNumber(memberSignUp.getPhoneNumber())
                    .snstype(SnsType.CATSNAP)
                    .build();
        }
    }
}
