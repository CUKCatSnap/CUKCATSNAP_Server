package com.cuk.catsnap.global.security.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.global.security.userdetail.MemberDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Member> member = memberRepository.findByIdentifier(username);
        return member.map(MemberDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Member not found")
                );
    }

    public Long getMemberId(String username) {
        Optional<Member> member = memberRepository.findByIdentifier(username);
        return member.map(Member::getId)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Member not found")
                );
    }
}
