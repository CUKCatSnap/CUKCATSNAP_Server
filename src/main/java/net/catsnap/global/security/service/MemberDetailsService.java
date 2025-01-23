package net.catsnap.global.security.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.global.security.userdetail.MemberDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
