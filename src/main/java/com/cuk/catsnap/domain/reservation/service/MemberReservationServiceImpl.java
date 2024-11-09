package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerReservationLocationRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerReservationNoticeRepository;
import com.cuk.catsnap.domain.photographer.service.PhotographerService;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramResponse;
import com.cuk.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.member.response.MemberReservationInformationListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.MemberReservationInformationResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerReservationGuidanceResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.ReservationBookResultResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationQueryType;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.ProgramRepository;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.Exception.reservation.CanNotReserveAfterDeadline;
import com.cuk.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import com.cuk.catsnap.global.Exception.reservation.DeletedProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import com.cuk.catsnap.global.Exception.reservation.OverLappingTimeException;
import com.cuk.catsnap.global.geography.converter.GeographyConverter;
import com.cuk.catsnap.global.result.SlicedData;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReservationServiceImpl implements MemberReservationService {

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

    @Override
    public ReservationBookResultResponse createReservation(
        MemberReservationRequest memberReservationRequest) {
        Long memberId = GetAuthenticationInfo.getUserId();
        LocalDate startDate = memberReservationRequest.startTime().toLocalDate();
        LocalTime startTime = memberReservationRequest.startTime().toLocalTime();
        Weekday weekday = getWeekday(startDate);
        Program program = programRepository.findByIdAndPhotographerId(
                memberReservationRequest.programId(), memberReservationRequest.photographerId())
            .orElseThrow(() -> new NotFoundProgramException("해당 작가의 프로그램이 존재하지 않습니다."));
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

    @Override
    public PhotographerAvailableReservationTimeListResponse getAvailableReservationTime(
        LocalDate date, Long photographerId) {
        Weekday weekday = getWeekday(date);
        String ReservationTimeFormatId = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographerId, weekday)
            .orElseThrow(() -> new OwnershipNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."))
            .getReservationTimeFormatId();
        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(
            ReservationTimeFormatId);
        if (reservationTimeFormat == null) {
            return PhotographerAvailableReservationTimeListResponse.from(Collections.emptyList());
        }

        /*
         * photographerStartTimeList는 작가가 설정한 예약 가능한 시간 목록입니다.(단 현재 시간 이후만 조회)
         * ReservationList는 현재까지 작가에게 예약된 예약 목록입니다.
         * 현재 예약 가능한 시간대를 조회하기 위해 예약된 시간대를 isAvailableReservation을 false로 설정하여 반환합니다.
         */
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList()
            .stream()
            .filter(time -> time.atDate(date).isAfter(LocalDateTime.now()))
            .toList();
        List<Reservation> reservationList = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(
            photographerId, LocalDateTime.of(date, LocalTime.MIN),
            LocalDateTime.of(date, LocalTime.MAX));

        List<PhotographerAvailableReservationTimeResponse> photographerAvailableReservationTimeResponseArrayList = new ArrayList<>();
        for (LocalTime startTime : photographerStartTimeList) {
            boolean isAvailableReservation = true;
            LocalDateTime startDateTime = LocalDateTime.now().toLocalDate().atTime(startTime);
            for (Reservation reservation : reservationList) {
                LocalDateTime reservationStartTime = reservation.getStartTime();
                LocalDateTime reservationEndTime = reservation.getEndTime();
                if (reservationStartTime.isAfter(startDateTime)
                    || reservationStartTime.isEqual(startDateTime)
                    && reservationEndTime.isBefore(startDateTime) || reservationEndTime.isEqual(
                    startDateTime)) {
                    isAvailableReservation = false;
                    break;
                }
            }
            photographerAvailableReservationTimeResponseArrayList.add(
                PhotographerAvailableReservationTimeResponse.of(startTime, isAvailableReservation));
        }

        return PhotographerAvailableReservationTimeListResponse.from(
            photographerAvailableReservationTimeResponseArrayList);
    }

    @Override
    public PhotographerProgramListResponse getPhotographerProgram(Long photographerId) {
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList());
    }

    @Override
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
            throw new OwnershipNotFoundException("해당 작가의 예약 전 주의사항 또는 예약 가능한 장소가 존재하지 않습니다.");
        }
        return PhotographerReservationGuidanceResponse.of(photographerLocation,
            photographerNotification);
    }

    @Override
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

    @Override
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

    @Override
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
     * todo : 공휴일을 체크하는 로직이 없음. 공휴일을 체크하는 로직을 추가해야함.
     */
    private Weekday getWeekday(LocalDate date) {
        return Weekday.valueOf(date.getDayOfWeek().name());
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
                () -> new OwnershipNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."));
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

        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getEndTime().isBefore(startTime)) {
                if (lastEndingBeforeStart.getEndTime().isBefore(reservation.getEndTime())) {
                    lastEndingBeforeStart = reservation;
                }
            }
            if (reservation.getStartTime().isAfter(endTime) && !firstStartingAfterEnd.getStartTime()
                .isEqual(LocalDateTime.MAX)) {
                firstStartingAfterEnd = reservation;
            }
        }

        Duration duration = Duration.between(firstStartingAfterEnd.getEndTime(),
            lastEndingBeforeStart.getStartTime());
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
