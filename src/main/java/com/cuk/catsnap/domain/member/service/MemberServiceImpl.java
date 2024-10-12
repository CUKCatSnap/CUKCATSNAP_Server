package com.cuk.catsnap.domain.member.service;

import com.cuk.catsnap.domain.member.converter.MemberConverter;
import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConverter memberConverter;

    @Override
    public void singUp(MemberRequest.MemberSignUp memberSignUp) {
        memberRepository.findByIdentifier(memberSignUp.getIdentifier())
                .ifPresent(member -> {
                    throw new DuplicatedMemberIdException("이미 존재하는 아이디입니다.");
                });

        Member singUpMember = memberConverter.memberRequestMemberSignUpToMember(memberSignUp, passwordEncoder.encode(memberSignUp.getPassword()));
        memberRepository.save(singUpMember);

        //todo 멤버 약관 동의 상태 저장
    }

    //약관 동의 상태와 필수 약관 동의 여부 확인
    private void userTermsAgreement(){

    }

}
