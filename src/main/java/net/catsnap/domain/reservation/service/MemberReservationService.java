package net.catsnap.domain.reservation.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.domain.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.photographer.service.PhotographerService;
import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import net.catsnap.domain.reservation.dto.MonthReservationCheckResponse;
import net.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import net.catsnap.domain.reservation.dto.member.response.MemberReservationInformationListResponse;
import net.catsnap.domain.reservation.dto.member.response.MemberReservationInformationResponse;
import net.catsnap.domain.reservation.dto.member.response.PhotographerReservationGuidanceResponse;
import net.catsnap.domain.reservation.dto.member.response.ReservationBookResultResponse;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationQueryType;
import net.catsnap.domain.reservation.entity.ReservationState;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import net.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.Exception.reservation.CanNotReserveAfterDeadline;
import net.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import net.catsnap.global.Exception.reservation.DeletedProgramException;
import net.catsnap.global.Exception.reservation.NotFoundProgramException;
import net.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import net.catsnap.global.Exception.reservation.OverLappingTimeException;
import net.catsnap.global.geography.converter.GeographyConverter;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReservationService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final ProgramRepository programRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;
    private final PhotographerReservationLocationRepository photographerReservationLocationRepository;
    private final PhotographerReservationNoticeRepository photographerReservationNoticeRepository;
    private final GeographyConverter geographyConverter;
    private final PhotographerService photographerService;
    private final WeekdayService weekdayService;

    public ReservationBookResultResponse createReservation(
        MemberReservationRequest memberReservationRequest) {
        Long memberId = GetAuthenticationInfo.getUserId();
        LocalDate startDate = memberReservationRequest.startTime().toLocalDate();
        LocalTime startTime = memberReservationRequest.startTime().toLocalTime();
        Weekday weekday = weekdayService.getWeekday(startDate);
        Program program = programRepository.findById(
                memberReservationRequest.programId())
            .orElseThrow(() -> new NotFoundProgramException("해당 작가의 프로그램이 존재하지 않습니다."));
        if (!program.getPhotographer().getId().equals(memberReservationRequest.photographerId())) {
            throw new OwnershipNotFoundException("해당 작가의 프로그램이 아닙니다.");
        }
        if (program.getDeleted()) {
            throw new DeletedProgramException("해당 작가의 프로그램이 삭제되었습니다.");
        }

        /*
         * 1. 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인
         * 2. 예약 시작 시간이 현재 시간보다 이후인지 확인
         * 3. 해당 일에 사용자가 원하는 예약 시작 시간이 존재하는지 확인
         * 4. 해당 일에 시간이 중복되는지 확인(작가 설정에 따라 중복 가능)
         */
        isAvailableReservationDate(startDate, memberReservationRequest.photographerId());
        isAfterNow(memberReservationRequest.startTime());
        isValidStartTimeInTimeFormat(startTime, weekday, memberReservationRequest.photographerId());
        isNotOverBooking(startDate, startTime, memberReservationRequest.photographerId(),
            program.getDurationMinutes());

        Member member = memberRepository.getReferenceById(memberId);
        Photographer photographer = photographerRepository.getReferenceById(
            memberReservationRequest.photographerId());

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
            .reservationState(ReservationState.APPROVED) // todo : 작가 설정에 따른 초기 예약 상태 설정 필요
            .build());

        return ReservationBookResultResponse.from(reservation);
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
        Pageable pageable) {
        Long memberId = GetAuthenticationInfo.getUserId();
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

    public MonthReservationCheckListResponse getReservationListByMonth(LocalDate month) {
        Long memberId = GetAuthenticationInfo.getUserId();
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
        LocalDate day) {
        Long memberId = GetAuthenticationInfo.getUserId();
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

    /*
     * 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인하는 메소드 입니다.
     */
    private boolean isAvailableReservationDate(LocalDate wantToReservationDate,
        Long photographerId) {
        Long preReservationDays = photographerService.findPhotographerSetting(photographerId)
            .getPreReservationDays();
        if (wantToReservationDate.isAfter(LocalDate.now().plusDays(preReservationDays))) {
            throw new CanNotReserveAfterDeadline(
                "해당 작가는 " + preReservationDays + "이후 까지만 예약이 가능합니다.");
        }
        return true;
    }

    /*
     * 해당 일에 사용자가 원하는 예약 시작 시간이 작가의 예약 시간 테이블에 존재하는지 확인하는 메소드 입니다.
     * wantToReservationTime는 HH:mm형식으로 들어옵니다.
     */
    private boolean isValidStartTimeInTimeFormat(LocalTime wantToReservationTime, Weekday weekday,
        Long photographerId) {
        WeekdayReservationTimeMapping weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographerId, weekday)
            .orElseThrow(
                () -> new ResourceNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."));
        String reservationTimeFormatId = weekdayReservationTimeMapping.getReservationTimeFormatId();
        /*
         * 해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않으면 예외 발생
         */
        if (reservationTimeFormatId == null) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다.");
        }

        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(
            reservationTimeFormatId);
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList();
        /*
         * 작가가 설정한 시작 시간과 일치하지 않으면 예외 발생
         */
        if (!photographerStartTimeList.contains(wantToReservationTime)) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 해당 시간이 존재하지 않습니다.");
        }
        return true;
    }

    /*
     * 해당 일에 해당 작가의 예약을 조회하고, 시간이 겹치는지 확인하는 메소드 입니다.
     */
    private boolean isNotOverBooking(LocalDate wantToReservationDate,
        LocalTime wantToReservationTime, Long photographerId, Long programDurationMinutes) {
        final int dayOfBothSide = 1;//만약, 예약 시간이 00시 이면, 전날도 고려해야 하므로 하루 전 예약 또한 고려 해야 한다.
        LocalDateTime startTime = LocalDateTime.of(wantToReservationDate.minusDays(dayOfBothSide),
            wantToReservationTime);
        LocalDateTime endTime = LocalDateTime.of(wantToReservationDate.plusDays(dayOfBothSide),
            wantToReservationTime);

        List<Reservation> reservationRepositoryList = reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
            photographerId, startTime, endTime);

        //reservationRepositoryList에서 종료 시간이 startTime보다 작은 값 중 가장 큰 값을 찾아야 한다.
        //reservationRepositoryList에서 시작 시간이 endTime보다 큰 값 중 가장 작은 값을 찾아야 한다.
        Reservation lastEndingBeforeStart = Reservation.builder().startTime(LocalDateTime.MIN)
            .endTime(LocalDateTime.MIN).build();
        Reservation firstStartingAfterEnd = Reservation.builder().startTime(LocalDateTime.MAX)
            .endTime(LocalDateTime.MAX).build();

        //처음과 마직막에 더미 데이터를 넣어준다.
        reservationRepositoryList.add(0,
            Reservation.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN)
                .build()); //불편...
        reservationRepositoryList.add(
            Reservation.builder().startTime(LocalDateTime.MAX).endTime(LocalDateTime.MAX).build());

        //겹치는 시간 자체가 없어야 한다.
        LocalDateTime wantToReservationDateTime = LocalDateTime.of(wantToReservationDate,
            wantToReservationTime);
        LocalDateTime wantToReservationDateTimeEnd = wantToReservationDateTime.plusMinutes(
            programDurationMinutes);
        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getStartTime().isAfter(wantToReservationDateTime)
                && reservation.getEndTime().isBefore(wantToReservationDateTimeEnd)) {
                throw new OverLappingTimeException("해당 시간대는 예약 중복으로 인해 예약이 불가능합니다.");
            }
            if (reservation.getStartTime().isEqual(wantToReservationDateTime)) {
                throw new OverLappingTimeException("해당 시간대는 예약 중복으로 인해 예약이 불가능합니다.");
            }
        }

        //reservationRepositoryList에서 종료 시간이 startTime보다 작은 값 중 가장 큰 값을 찾아야 한다.
        //reservationRepositoryList에서 시작 시간이 endTime보다 큰 값 중 가장 작은 값을 찾아야 한다.
        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getEndTime().isBefore(startTime)) {
                if (lastEndingBeforeStart.getEndTime().isBefore(reservation.getEndTime())) {
                    lastEndingBeforeStart = reservation;
                }
            }
            if (reservation.getStartTime().isAfter(endTime)
                && !firstStartingAfterEnd.getStartTime().isEqual(LocalDateTime.MAX)) {
                firstStartingAfterEnd = reservation;
            }
        }

        Duration duration = Duration.between(lastEndingBeforeStart.getEndTime(),
            firstStartingAfterEnd.getStartTime());
        if (duration.toMinutes() >= programDurationMinutes) {
            return true;
        } else {
            throw new OverLappingTimeException("해당 시간대는 예약 중복으로 인해 예약이 불가능합니다.");
        }
    }

    private boolean isAfterNow(LocalDateTime startTime) {
        if (startTime.isAfter(LocalDateTime.now())) {
            return true;
        } else {
            throw new CanNotStartTimeBeforeNow("현재 시간 이전의 예약은 불가능합니다.");
        }
    }
}
