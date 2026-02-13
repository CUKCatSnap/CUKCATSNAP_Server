package net.catsnap.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import net.catsnap.domain.reservation.dto.MonthReservationCheckResponse;
import net.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import net.catsnap.domain.reservation.dto.member.response.MemberReservationInformationListResponse;
import net.catsnap.domain.reservation.dto.member.response.MemberReservationInformationResponse;
import net.catsnap.domain.reservation.dto.member.response.PhotographerReservationGuidanceResponse;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationQueryType;
import net.catsnap.domain.reservation.entity.ReservationState;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.domain.user.photographer.document.PhotographerSetting;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.user.photographer.service.PhotographerService;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.Exception.reservation.CanNotReserveAfterDeadline;
import net.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import net.catsnap.global.Exception.reservation.DeletedProgramException;
import net.catsnap.global.Exception.reservation.NotFoundProgramException;
import net.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import net.catsnap.global.Exception.reservation.OverLappingTimeException;
import net.catsnap.global.geography.converter.GeographyConverter;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReservationService {

    private final ProgramRepository programRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;
    private final PhotographerReservationLocationRepository photographerReservationLocationRepository;
    private final PhotographerReservationNoticeRepository photographerReservationNoticeRepository;
    private final GeographyConverter geographyConverter;
    private final PhotographerService photographerService;
    private final ReservationValidatorService reservationValidatorService;

    public Reservation createReservation(
        MemberReservationRequest memberReservationRequest, long memberId) {
        PhotographerSetting photographerSetting = photographerService.findPhotographerSetting(
            memberReservationRequest.photographerId());
        Program program = programRepository.findById(
                memberReservationRequest.programId())
            .orElseThrow(() -> new NotFoundProgramException("해당 작가의 프로그램이 존재하지 않습니다."));
        program.checkOwnership(memberReservationRequest.photographerId());
        if (program.getDeleted()) {
            throw new DeletedProgramException("해당 작가의 프로그램이 삭제되었습니다.");
        }

        reservationRepository.acquireReservationLock(memberReservationRequest.photographerId());

        /*
         * 1. 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인
         * 2. 예약 시작 시간이 현재 시간보다 이후인지 확인
         * 3. 해당 일에 사용자가 원하는 예약 시작 시간이 존재하는지 확인
         * 4. 해당 일에 시간이 중복되는지 확인(작가 설정에 따라 중복 가능)
         */
        if (!reservationValidatorService.isWithinAllowedDays(memberReservationRequest.startTime(),
            photographerSetting)) {
            throw new CanNotReserveAfterDeadline("해당 작가는 해당 일에 예약을 받을 수 없습니다.");
        }
        if (!reservationValidatorService.isAfterNow(memberReservationRequest.startTime())) {
            throw new CanNotStartTimeBeforeNow("현재 시간 이전의 예약은 불가능합니다.");
        }
        if (!reservationValidatorService.isValidStartTimeInTimeFormat(
            memberReservationRequest.startTime(), memberReservationRequest.photographerId())) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 해당 시간이 존재하지 않습니다.");
        }
        if (!reservationValidatorService.isNotOverBooking(memberReservationRequest.startTime(),
            memberReservationRequest.photographerId(),
            program.getDurationMinutes())) {
            throw new OverLappingTimeException("해당 시간대는 예약 중복으로 인해 예약이 불가능합니다.");
        }

        Member member = memberRepository.getReferenceById(memberId);
        Photographer photographer = photographerRepository.getReferenceById(
            memberReservationRequest.photographerId());

        boolean isAutoReservationAccept = photographerService.findPhotographerSetting(
            memberReservationRequest.photographerId()).getAutoReservationAccept();
        ReservationState reservationState =
            isAutoReservationAccept ? ReservationState.APPROVED : ReservationState.PENDING;

        Reservation reservation = reservationRepository.save(Reservation.builder()
            .member(member)
            .photographer(photographer)
            .program(program)
            .location(geographyConverter.toPoint(
                memberReservationRequest.reservationLocation().latitude(),
                memberReservationRequest.reservationLocation().longitude()))
            .locationName(memberReservationRequest.reservationLocation().locationName())
            .startTime(memberReservationRequest.startTime())
            .endTime(memberReservationRequest.startTime().plusMinutes(program.getDurationMinutes()))
            .reservationState(reservationState)
            .build());

        return reservation;
    }

    public PhotographerReservationGuidanceResponse getPhotographerReservationGuidance(
        Long photographerId) {
        String photographerNotification = "";
        String photographerLocation = "";
        try {
            photographerNotification = photographerReservationNoticeRepository.findByPhotographerId(
                photographerId).getContent();
            photographerLocation = photographerReservationLocationRepository.findByPhotographerId(
                photographerId).getContent();
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("해당 작가의 예약 전 주의사항 또는 예약 가능한 장소가 존재하지 않습니다.");
        }
        return PhotographerReservationGuidanceResponse.of(photographerLocation,
            photographerNotification);
    }

    public SlicedData<MemberReservationInformationListResponse> getMyReservation(
        ReservationQueryType reservationQueryType,
        Pageable pageable, long memberId) {
        Slice<Reservation> reservationSlice = null;

        if (reservationQueryType.equals(ReservationQueryType.ALL)) {
            reservationSlice = reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId,
                pageable);
        } else if (reservationQueryType.equals(ReservationQueryType.UPCOMING)) {
            LocalDateTime now = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
            reservationSlice = reservationRepository.findAllByMemberIdAndStartTimeAfterAndReservationStateInOrderByStartTimeAsc(
                memberId, now, List.of(ReservationState.APPROVED, ReservationState.PENDING),
                pageable);
        }

        MemberReservationInformationListResponse memberReservationInformationListResponse = MemberReservationInformationListResponse.from(
            reservationSlice.getContent()
                .stream()
                .map(MemberReservationInformationResponse::from)
                .toList());
        return SlicedData.of(memberReservationInformationListResponse, reservationSlice.isFirst(),
            reservationSlice.isLast());
    }

    public MonthReservationCheckListResponse getReservationListByMonth(LocalDate month,
        long memberId) {
        LocalDateTime startOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(), 1, 0,
            0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(),
            month.lengthOfMonth(), 23, 59, 59);

        List<Reservation> reservationList = reservationRepository.findAllReservationByMemberIdAndStartTimeBetween(
            memberId,
            startOfMonth, endOfMonth);

        return MonthReservationCheckListResponse.from(
            reservationList.stream()
                .map(MonthReservationCheckResponse::from)
                .toList()
        );
    }

    public MemberReservationInformationListResponse getReservationDetailListByDay(
        LocalDate day, long memberId) {
        LocalDateTime startOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 23, 59, 59);
        List<Reservation> reservationList = reservationRepository.findAllReservationWithEagerByMemberIdAndStartTimeBetween(
            memberId, startOfDay, endOfDay);
        List<MemberReservationInformationResponse> reservationListResponse = reservationList.stream()
            .map(MemberReservationInformationResponse::from)
            .toList();
        return MemberReservationInformationListResponse.from(reservationListResponse);
    }
}
