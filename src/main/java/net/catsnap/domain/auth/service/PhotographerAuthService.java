package net.catsnap.domain.auth.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.dto.photographer.request.PhotographerSignUpRequest;
import net.catsnap.domain.reservation.service.PhotographerReservationService;
import net.catsnap.domain.user.photographer.document.PhotographerSetting;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerSettingRepository;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotographerRepository photographerRepository;
    private final PhotographerSettingRepository photographerSettingRepository;
    private final PhotographerReservationNoticeRepository photographerReservationNoticeRepository;
    private final PhotographerReservationLocationRepository photographerReservationLocationRepository;

    private final PhotographerReservationService photographerReservationService;

    @Transactional
    public void singUp(PhotographerSignUpRequest photographerSignUpRequest) {
        userRepository.findByIdentifier(photographerSignUpRequest.identifier())
            .ifPresent(user -> {
                throw new DuplicatedPhotographerException("이미 존재하는 아이디입니다.");
            });

        String encodedPassword = passwordEncoder.encode(photographerSignUpRequest.password());
        Photographer photographer = photographerSignUpRequest.toEntity(encodedPassword);
        photographerRepository.save(photographer);

        /*
         * 작가 회원가입 시 초기화 작업
         */
        // weekdayReservationTimeMapping 생성 (예약을 형식을 요일에 매핑하는 테이블 생성)
        photographerReservationService.createJoinedPhotographerReservationTimeFormat(photographer);
        // photographerSetting 초기화
        initializeSineUpPhotographer(photographer.getId());
        // todo : 이용약관 동의 여부 확인
    }

    /*
     * 작가 회원가입 시 초기화 작업
     */
    public void initializeSineUpPhotographer(Long photographerId) {
        PhotographerSetting photographerSetting = PhotographerSetting.builder()
            .photographerId(photographerId)
            .autoReservationAccept(false)
            .enableOverBooking(false)
            .preReservationDays(14L)
            .build();
        photographerSettingRepository.save(photographerSetting);
        photographerReservationNoticeRepository.save("", photographerId);
        photographerReservationLocationRepository.save("", photographerId);
    }
}
