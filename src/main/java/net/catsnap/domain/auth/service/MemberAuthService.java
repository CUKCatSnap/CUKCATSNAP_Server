package net.catsnap.domain.auth.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.dto.member.request.MemberSignUpRequest;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.global.Exception.member.DuplicatedMemberIdException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void singUp(MemberSignUpRequest memberSignUpRequest) {
        memberRepository.findByIdentifier(memberSignUpRequest.identifier())
            .ifPresent(member -> {
                throw new DuplicatedMemberIdException("이미 존재하는 아이디입니다.");
            });
        String hashedPassword = passwordEncoder.encode(memberSignUpRequest.password());
        Member singUpMember = memberSignUpRequest.toEntity(hashedPassword);
        memberRepository.save(singUpMember);
        //todo 멤버 약관 동의 상태 저장
    }

    //약관 동의 상태와 필수 약관 동의 여부 확인
    private void userTermsAgreement() {

    }
}
