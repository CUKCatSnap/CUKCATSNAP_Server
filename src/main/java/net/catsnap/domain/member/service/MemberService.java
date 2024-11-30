package net.catsnap.domain.member.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberTinyInformationResponse getMemberTinyInformation(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 회원입니다."));

        return MemberTinyInformationResponse.from(member);
    }

}
