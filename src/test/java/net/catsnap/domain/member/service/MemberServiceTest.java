package net.catsnap.domain.member.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.support.fixture.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    class 사용자의_tinyInformation을_조회하는_기능 {

        @Test
        void 사용자의_tiny_information을_조회한다() {
            // given
            Member member = MemberFixture.member()
                .id(1L)
                .build();
            given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

            // when
            MemberTinyInformationResponse memberTinyInformation
                = memberService.getMemberTinyInformation(member.getId());

            // then
            Assertions.assertThat(memberTinyInformation.nickname()).isEqualTo(member.getNickname());
            Assertions.assertThat(memberTinyInformation.profilePhotoUrl())
                .isEqualTo(member.getProfilePhotoUrl());
        }

        @Test
        void 존재하지_않는_회원의_tiny_information을_조회하면_예외가_발생한다() {
            Member member = MemberFixture.member()
                .id(1L)
                .build();
            given(memberRepository.findById(member.getId()))
                .willReturn(Optional.empty());

            // when, then
            Assertions.assertThatThrownBy(
                    () -> memberService.getMemberTinyInformation(member.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        }
    }
}