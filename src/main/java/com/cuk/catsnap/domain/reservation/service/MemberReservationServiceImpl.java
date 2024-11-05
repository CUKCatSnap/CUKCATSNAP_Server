package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerReservationLocationRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerReservationNoticeRepository;
import com.cuk.catsnap.domain.reservation.converter.ReservationConverter;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
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
import com.cuk.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import com.cuk.catsnap.global.Exception.reservation.DeletedProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import com.cuk.catsnap.global.Exception.reservation.OverLappingTimeException;
import com.cuk.catsnap.global.geography.converter.GeographyConverter;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

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
    private final ReservationConverter reservationConverter;

    @Override
    public Reservation createReservation(ReservationRequest.ReservationBook reservationBook) {
        Long memberId = GetAuthenticationInfo.getUserId();
        LocalDate startDate = reservationBook.getStartTime().toLocalDate();
        LocalTime startTime = reservationBook.getStartTime().toLocalTime();
        Weekday weekday = getWeekday(startDate);
        Program program = programRepository.findByIdAndPhotographerId(reservationBook.getProgramId(), reservationBook.getPhotographerId())
                .orElseThrow(() -> new NotFoundProgramException("해당 작가의 프로그램이 존재하지 않습니다."));
        if(program.getDeleted()) {
            throw new DeletedProgramException("해당 작가의 프로그램이 삭제되었습니다.");
        }

        /*
         * 1. 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인
         * 2. 예약 시작 시간이 현재 시간보다 이후인지 확인
         * 3. 해당 일에 사용자가 원하는 예약 시작 시간이 존재하는지 확인
         * 4. 해당 일에 시간이 중복되는지 확인(작가 설정에 따라 중복 가능)
         */
        isAvailableReservationDate(startDate, reservationBook.getPhotographerId());
        isAfterNow(reservationBook.getStartTime());
        isValidStartTimeInTimeFormat(startTime, weekday, reservationBook.getPhotographerId());
        isNotOverBooking(startDate, startTime, reservationBook.getPhotographerId(), program.getDurationMinutes());

        Member member = memberRepository.getReferenceById(memberId);
        Photographer photographer = photographerRepository.getReferenceById(reservationBook.getPhotographerId());

        return reservationRepository.save(Reservation.builder()
                .member(member)
                .photographer(photographer)
                .program(program)
                .location(geographyConverter.toPoint(reservationBook.getLocation().getLatitude(), reservationBook.getLocation().getLongitude()))
                .locationName(reservationBook.getLocation().getLocationName())
                .startTime(reservationBook.getStartTime())
                .endTime(reservationBook.getStartTime().plusMinutes(program.getDurationMinutes()))
                .reservationState(ReservationState.APPROVED) // todo : 작가 설정에 따른 초기 예약 상태 설정 필요
                .build());
    }

    @Override
    public ReservationResponse.PhotographerAvailableReservationTimeList getAvailableReservationTime(LocalDate date, Long photographerId) {
        Weekday weekday = getWeekday(date);
        String ReservationTimeFormatId = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(photographerId, weekday)
                .orElseThrow(() -> new OwnershipNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."))
                .getReservationTimeFormatId();
        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(ReservationTimeFormatId);
        if(reservationTimeFormat == null) {
            return ReservationResponse.PhotographerAvailableReservationTimeList.builder()
                    .photographerAvailableReservationTimeList(Collections.emptyList())
                    .build();
        }
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList().stream()
                .filter(time -> time.atDate(date).isAfter(LocalDateTime.now()))
                .toList();
        List<Reservation> reservationList = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(photographerId, LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
        return reservationConverter.toPhotographerAvailableReservationTimeList(photographerStartTimeList, reservationList);
    }

    @Override
    public List<Program> getPhotographerProgram(Long photographerId) {
        return programRepository.findByPhotographerIdAndDeletedFalse(photographerId);
    }

    @Override
    public ReservationResponse.PhotographerReservationGuidance getPhotographerReservationGuidance(Long photographerId) {
        String photographerNotification = "";
        String photographerLocation = "";
        try{
            photographerNotification = photographerReservationNoticeRepository.findByPhotographerId(photographerId).getContent();
            photographerLocation = photographerReservationLocationRepository.findByPhotographerId(photographerId).getContent();
        } catch (NullPointerException e) {
            throw new OwnershipNotFoundException("해당 작가의 예약 전 주의사항 또는 예약 가능한 장소가 존재하지 않습니다.");
        }
        return ReservationResponse.PhotographerReservationGuidance.builder()
                .photographerNotification(photographerNotification)
                .photographerLocation(photographerLocation)
                .build();
    }

    @Override
    public Slice<Reservation> getMyReservation(ReservationQueryType reservationQueryType, Pageable pageable) {
        Long memberId = GetAuthenticationInfo.getUserId();

        if(reservationQueryType.equals(ReservationQueryType.ALL)) {
            return reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId, pageable);
        } else if(reservationQueryType.equals(ReservationQueryType.UPCOMING)) {
            LocalDateTime now = LocalDateTime.now()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            return reservationRepository.findAllByMemberIdAndStartTimeAfterAndReservationStateInOrderByStartTimeAsc(memberId, now, List.of(ReservationState.APPROVED, ReservationState.PENDING), pageable);
        }

        throw new IllegalArgumentException("올바른 예약 조회 타입이 아닙니다.");
    }

    @Override
    public List<Reservation> getReservationListByMonth(LocalDate month) {
        Long memberId = GetAuthenticationInfo.getUserId();
        LocalDateTime startOfMonth = LocalDateTime.of(month.getYear(),month.getMonthValue(),1,0,0,0);
        LocalDateTime endOfMonth = LocalDateTime.of(month.getYear(),month.getMonthValue(),month.lengthOfMonth(),23,59,59);
        return reservationRepository.findAllReservationByMemberIdAndStartTimeBetween(memberId, startOfMonth, endOfMonth);
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
    private boolean isAvailableReservationDate(LocalDate wantToReservationDate, Long photographerId) {
        /*
        * todo : 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인하는 로직이 필요합니다.
        * 아직 작가의 예약 설정 로직이 미개발입니다..
         */
        return true;
    }

    /*
    * 해당 일에 사용자가 원하는 예약 시작 시간이 작가의 예약 시간 테이블에 존재하는지 확인하는 메소드 입니다.
    * wantToReservationTime는 HH:mm형식으로 들어옵니다.
     */
    private boolean isValidStartTimeInTimeFormat(LocalTime wantToReservationTime, Weekday weekday, Long photographerId) {
        WeekdayReservationTimeMapping weekdayReservationTimeMapping =  weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(photographerId, weekday)
                .orElseThrow(() -> new OwnershipNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."));
        String reservationTimeFormatId = weekdayReservationTimeMapping.getReservationTimeFormatId();
        /*
        * 해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않으면 예외 발생
         */
        if(reservationTimeFormatId == null) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다.");
        }

        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(reservationTimeFormatId);
        List<LocalTime>photographerStartTimeList = reservationTimeFormat.getStartTimeList();
        /*
         * 작가가 설정한 시작 시간과 일치하지 않으면 예외 발생
         */
        if(!photographerStartTimeList.contains(wantToReservationTime)) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 해당 시간이 존재하지 않습니다.");
        }
        return true;
    }

    /*
    * 해당 일에 해당 작가의 예약을 조회하고, 시간이 겹치는지 확인하는 메소드 입니다.
     */
    private boolean isNotOverBooking (LocalDate wantToReservationDate, LocalTime wantToReservationTime, Long photographerId, Long programDurationMinutes) {
        final int dayOfBothSide = 1;//만약, 예약 시간이 00시 이면, 전날도 고려해야 하므로 하루 전 예약 또한 고려 해야 한다.
        LocalDateTime startTime = LocalDateTime.of(wantToReservationDate.minusDays(dayOfBothSide), wantToReservationTime);
        LocalDateTime endTime = LocalDateTime.of(wantToReservationDate.plusDays(dayOfBothSide), wantToReservationTime);

        List<Reservation> reservationRepositoryList = reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(photographerId, startTime, endTime);

        //reservationRepositoryList에서 종료 시간이 startTime보다 작은 값 중 가장 큰 값을 찾아야 한다.
        //reservationRepositoryList에서 시작 시간이 endTime보다 큰 값 중 가장 작은 값을 찾아야 한다.
        Reservation lastEndingBeforeStart = Reservation.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN).build();
        Reservation firstStartingAfterEnd = Reservation.builder().startTime(LocalDateTime.MAX).endTime(LocalDateTime.MAX).build();

        //처음과 마직막에 더미 데이터를 넣어준다.
        reservationRepositoryList.add(0, Reservation.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN).build()); //불편...
        reservationRepositoryList.add(Reservation.builder().startTime(LocalDateTime.MAX).endTime(LocalDateTime.MAX).build());

        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getEndTime().isBefore(startTime)) {
                if (lastEndingBeforeStart.getEndTime().isBefore(reservation.getEndTime())) {
                    lastEndingBeforeStart = reservation;
                }
            }
            if (reservation.getStartTime().isAfter(endTime) && !firstStartingAfterEnd.getStartTime().isEqual(LocalDateTime.MAX)) {
                firstStartingAfterEnd = reservation;
            }
        }

        Duration duration = Duration.between(firstStartingAfterEnd.getEndTime(), lastEndingBeforeStart.getStartTime());
        if(duration.toMinutes() >= programDurationMinutes){
            return true;
        } else {
            throw new OverLappingTimeException("해당 시간대는 예약 중복으로 인해 예약이 불가능합니다.");
        }
    }

    private boolean isAfterNow(LocalDateTime startTime) {
        if(startTime.isAfter(LocalDateTime.now())){
            return true;
        } else {
            throw new CanNotStartTimeBeforeNow("현재 시간 이전의 예약은 불가능합니다.");
        }
    }
}
