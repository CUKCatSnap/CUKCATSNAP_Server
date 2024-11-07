package com.cuk.catsnap.global.security.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Tag("member_login")
@ExtendWith(MockitoExtension.class)
class MemberDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("멤버 아이디로 찾기-해당 아이디가 존재함")
    void loadUserByUsername() {
        //given
        Member member = Member.builder()
            .identifier("test")
            .password("encoded_password")
            .build();

        Mockito.when(memberRepository.findByIdentifier("test"))
            .thenReturn(Optional.of(member));

        //when
        UserDetails userDetails = memberDetailsService.loadUserByUsername(member.getIdentifier());

        //then
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(member.getIdentifier());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(member.getPassword());
        Assertions.assertThat(userDetails.getAuthorities())
            .isEqualTo(List.of(new SimpleGrantedAuthority("ROLE_MEMBER")));
        Mockito.verify(memberRepository).findByIdentifier("test");
    }

    @Test
    @DisplayName("멤버 아이디로 멤버 찾기 - 해당 아이디가 존재하지 않음")
    void loadUserByUsernameNull() {
        //given
        Mockito.when(memberRepository.findByIdentifier("not_exist_member"))
            .thenReturn(Optional.empty());

        //when,then
        Assertions.assertThatThrownBy(
                () -> memberDetailsService.loadUserByUsername("not_exist_member"))
            .isInstanceOf(UsernameNotFoundException.class);
        Mockito.verify(memberRepository).findByIdentifier("not_exist_member");
    }
}