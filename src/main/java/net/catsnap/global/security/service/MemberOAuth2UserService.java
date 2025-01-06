package net.catsnap.global.security.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.global.security.dto.OAuth2MemberResponse;
import net.catsnap.global.security.oauth2user.MemberOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class MemberOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2MemberResponse oAuth2MemberResponse = null;
        if (clientId.equals("naver")) {
            oAuth2MemberResponse = OAuth2MemberResponse.fromNaver(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 클라이언트입니다.");
        }
        Optional<Member> member = memberRepository.findBySnsIdAndSnstype(
            oAuth2MemberResponse.snsId(),
            oAuth2MemberResponse.snsType());
        if (member.isPresent()) {
            return new MemberOAuth2User(member.get().getId(), oAuth2MemberResponse);
        } else {
            Member signInMember = memberRepository.save(oAuth2MemberResponse.toEntity());
            return new MemberOAuth2User(signInMember.getId(), oAuth2MemberResponse);
        }
    }
}
