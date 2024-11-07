package com.cuk.catsnap.domain.member.service;

import com.cuk.catsnap.domain.member.converter.MemberConverter;
import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberIdException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@Tag("member_login")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberConverter memberConverter;

    @Test
    @DisplayName("singUp() : 회원가입 테스트 - 성공 ")
    public void singUp() {
        //given
        MemberRequest.MemberSignUp memberSignUp = MemberRequest.MemberSignUp.builder()
            .identifier("id")
            .password("password")
            .build();

        Member member = Member.builder()
            .identifier("id")
            .password("encoded_password")
            .build();

        Mockito.when(memberRepository.save(member))
            .thenReturn(Member.builder()
                .id(1L)
                .identifier("id")
                .password("encoded_password")
                .build());

        Mockito.when(passwordEncoder.encode("password"))
            .thenReturn("encoded_password");

        Mockito.when(
                memberConverter.memberRequestMemberSignUpToMember(memberSignUp, "encoded_password"))
            .thenReturn(member);

        //when
        memberService.singUp(memberSignUp);

        //then
        Mockito.verify(passwordEncoder).encode(memberSignUp.getPassword());
        Mockito.verify(memberRepository).save(Mockito.any(Member.class));
    }

    @Test
    @DisplayName("singUp() :회원가입 테스트 - 아이디 중복으로 실패")
    public void duplicatedMemberId() {
        //given
        MemberRequest.MemberSignUp memberSignUp = MemberRequest.MemberSignUp.builder()
            .identifier("duplicatedId")
            .password("password")
            .build();

        Mockito.when(memberRepository.findByIdentifier("duplicatedId"))
            .thenReturn(Optional.of(Member.builder()
                .identifier("duplicatedId")
                .password("encoded_password")
                .build()));

        //when, then
        Assertions.assertThatThrownBy(() -> memberService.singUp(memberSignUp))
            .isInstanceOf(DuplicatedMemberIdException.class);
        Mockito.verify(memberRepository, Mockito.never()).save(Mockito.any(Member.class));
    }
}