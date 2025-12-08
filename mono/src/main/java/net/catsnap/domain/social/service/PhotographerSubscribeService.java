package net.catsnap.domain.social.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.social.entity.PhotographerSubscribe;
import net.catsnap.domain.social.repository.PhotographerSubscribeRepository;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerSubscribeService {

    private final PhotographerSubscribeRepository photographerSubscribeRepository;
    private final PhotographerRepository photographerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void toggleSubscribePhotographer(Long photographerId, Long memberId) {
        Photographer photographer = photographerRepository.findById(photographerId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 작가가 존재하지 않습니다."));

        Member member = memberRepository.getReferenceById(memberId);

        photographerSubscribeRepository.findByPhotographerIdAndMemberId(photographerId, memberId)
            .ifPresentOrElse(
                photographerSubscribeRepository::delete,
                () -> photographerSubscribeRepository.save(
                    new PhotographerSubscribe(member, photographer)
                )
            );
    }
}
