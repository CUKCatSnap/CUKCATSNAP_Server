package net.catsnap.domain.auth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import net.catsnap.domain.auth.dto.member.request.MemberSignUpRequest;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.global.Exception.member.DuplicatedMemberIdException;
import net.catsnap.support.fixture.MemberFixture;
import net.catsnap.support.fixture.MemberSignUpRequestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAuthServiceTest {

    @InjectMocks
    private MemberAuthService memberAuthService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class 사용자_회웝_가입 {

        @Test
        void 회원_가입_성공() {
            //given
            MemberSignUpRequest memberSignUpRequest = MemberSignUpRequestFixture.memberSignUpRequest()
                .password("rawPassword")
                .build();
            given(memberRepository.findByIdentifier(memberSignUpRequest.identifier())).willReturn(
                Optional.empty());
            given(passwordEncoder.encode(memberSignUpRequest.password())).willReturn(
                "hashedPassword");

            //when
            memberAuthService.singUp(memberSignUpRequest);

            //then
            verify(memberRepository, times(1))
                .save(argThat(member -> member.getPassword().equals("hashedPassword")));
        }

        @Test
        void 중복된_아이디로_회원_가입_실패() {
            //given
            MemberSignUpRequest memberSignUpRequest = MemberSignUpRequestFixture.memberSignUpRequest()
                .password("rawPassword")
                .build();
            Member member = MemberFixture.member()
                .identifier(memberSignUpRequest.identifier())
                .build();
            given(memberRepository.findByIdentifier(memberSignUpRequest.identifier())).willReturn(
                Optional.of(member));

            //when, then
            Assertions.assertThatThrownBy(
                () -> memberAuthService.singUp(memberSignUpRequest)
            ).isInstanceOf(DuplicatedMemberIdException.class);

            //then
            verify(memberRepository, times(0))
                .save(any());
        }
    }

}