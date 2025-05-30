package net.catsnap.domain.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.dto.photographer.request.PhotographerSignUpRequest;
import net.catsnap.domain.notification.entity.NotificationLastRead;
import net.catsnap.domain.notification.repository.NotificationLastReadRepository;
import net.catsnap.domain.reservation.service.PhotographerReservationService;
import net.catsnap.domain.user.photographer.document.PhotographerSetting;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerSettingRepository;
import net.catsnap.domain.user.photographer.service.PhotographerIntroductionService;
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
    private final NotificationLastReadRepository notificationLastReadRepository;

    private final PhotographerReservationService photographerReservationService;
    private final PhotographerIntroductionService photographerIntroductionService;

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

        Photographer photographer = photographerRepository.getReferenceById(photographerId);
        photographerIntroductionService.initPhotographerIntroduction(photographer);

        // 알림 마지막 읽은 시간 초기화
        NotificationLastRead notificationLastRead = NotificationLastRead.builder().
            user(photographer)
            .lastReadAt(LocalDateTime.now())
            .build();
        notificationLastReadRepository.save(notificationLastRead);
    }
}
