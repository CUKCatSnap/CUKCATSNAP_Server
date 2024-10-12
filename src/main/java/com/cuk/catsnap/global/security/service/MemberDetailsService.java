package com.cuk.catsnap.global.security.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.global.security.userdetail.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Member> member = memberRepository.findByIdentifier(username);
        if(member.isEmpty()) {
            throw new UsernameNotFoundException("Member not found");
        }
        return new MemberDetails(member.get());
    }

    public Long getMemberId(String username) {
        Optional<Member> member = memberRepository.findByIdentifier(username);
        if(member.isEmpty()) {
            throw new UsernameNotFoundException("Member not found");
        }
        return member.get().getId();
    }
}
