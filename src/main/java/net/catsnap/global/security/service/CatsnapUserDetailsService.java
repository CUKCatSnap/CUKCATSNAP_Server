package net.catsnap.global.security.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.security.userdetail.CatsnapUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Transactional
@RequiredArgsConstructor
public class CatsnapDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByIdentifier(username)
            .map(CatsnapUserDetails::new)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found")
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
