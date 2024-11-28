package net.catsnap.domain.member.service;

import net.catsnap.domain.member.converter.MemberConverter;
import net.catsnap.domain.member.dto.MemberRequest;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.global.Exception.member.DuplicatedMemberIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConverter memberConverter;

    public void singUp(MemberRequest.MemberSignUp memberSignUp) {
        memberRepository.findByIdentifier(memberSignUp.getIdentifier())
            .ifPresent(member -> {
                throw new DuplicatedMemberIdException("이미 존재하는 아이디입니다.");
            });

        Member singUpMember = memberConverter.memberRequestMemberSignUpToMember(memberSignUp,
            passwordEncoder.encode(memberSignUp.getPassword()));
        memberRepository.save(singUpMember);

        //todo 멤버 약관 동의 상태 저장
    }

    //약관 동의 상태와 필수 약관 동의 여부 확인
    private void userTermsAgreement() {

    }

}