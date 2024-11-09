package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.request.ProgramRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.request.ReservationTimeFormatRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationListResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatIdResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatListResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.photographerProgramIdResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.ProgramRepository;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.Exception.reservation.CanNotChangeReservationState;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotographerReservationServiceImpl implements PhotographerReservationService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final ProgramRepository programRepository;
    private final PhotographerRepository photographerRepository;
    private final ReservationRepository reservationRepository;

    /*
     * 새로운 작가가 회원가입을 하면, 각 요일에 대한 예약 테이블을 생성한다.
     * 작가는 예약 시간 형식을 만들 수 있고, 이 형식을 특정 요일에 매핑시켜야 한다.
     * 따라서 작가의 예약 시간 형식을 요일에 매핑시키기 위해 요일 - 형식 테이블을 생성한다.
     */
    public void createJoinedPhotographerReservationTimeFormat(Photographer photographer) {
        //추후 벌크 삽입으로 변경 예정
        List<WeekdayReservationTimeMapping> weekdayReservationTimeMappingList
            = Arrays.stream(Weekday.values())
            .map(weekday -> WeekdayReservationTimeMapping.builder()
                .photographer(photographer)
                .weekday(weekday)
                .build()
            )
            .toList();
        weekdayReservationTimeMappingRepository.saveAll(weekdayReservationTimeMappingList);
    }

    @Override
    public ReservationTimeFormatIdResponse createReservationTimeFormat(
        ReservationTimeFormatRequest reservationTimeFormatRequest, String reservationTimeFormatId) {

        Long photographerId = GetAuthenticationInfo.getUserId();
        ReservationTimeFormat reservationTimeFormat = null;

        /*
         *reservationTimeFormatId가 null이면 새로운 ReservationTimeFormat 생성이고,
         * null이 아니라면 기존의 ReservationTimeFormat 업데이트.
         */
        if (reservationTimeFormatId == null) {
            reservationTimeFormat = reservationTimeFormatRequest.toEntity(photographerId);
            reservationTimeFormatRepository.save(reservationTimeFormat);
        } else {
            reservationTimeFormat = reservationTimeFormatRequest.toEntity(reservationTimeFormatId,
                photographerId);
            UpdateResult updateResult = reservationTimeFormatRepository.update(
                reservationTimeFormat);
            if (updateResult.getModifiedCount() == 0) {
                throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
            }
        }
        return ReservationTimeFormatIdResponse.from(reservationTimeFormat);
    }

    @Override
    public ReservationTimeFormatListResponse getMyReservationTimeFormatList() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        List<ReservationTimeFormat> reservationTimeFormatList = reservationTimeFormatRepository.findByPhotographerId(
            photographerId);
        return ReservationTimeFormatListResponse.from(
            reservationTimeFormatList.stream()
                .map(ReservationTimeFormatResponse::from)
                .toList());

    }

    @Override
    public void deleteReservationTimeFormat(String reservationTimeFormatId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        DeleteResult deleteResult = reservationTimeFormatRepository.deleteById(
            reservationTimeFormatId, photographerId);
        if (deleteResult.getDeletedCount() == 0) {
            throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
        }
        // 해당 time format을 이용하고 있는 요일의 매핑을 null로 초기화
        weekdayReservationTimeMappingRepository.updateReservationTimeFormatIdToNull(photographerId,
            reservationTimeFormatId);
    }

    /*
     * 요일을 예약 시간 형식에 매핑시킨다. (요일을 고정. 예약 시간을 요일에 매핑시키는 것이다.)
     */
    @Override
    public void mappingWeekdayToReservationTimeFormat(String reservationTimeFormatId,
        Weekday weekday) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Optional<WeekdayReservationTimeMapping> weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
            photographerId, weekday);
        weekdayReservationTimeMapping.ifPresentOrElse(mapping ->
                mapping.updateReservationTimeFormatId(reservationTimeFormatId),
            () -> new OwnershipNotFoundException("내가 소유한 요일 중, 해당 요일을 찾을 수 없습니다.")
        );
    }

    @Override
    public void unmappingWeekdayToReservationTimeFormatByWeekday(Weekday weekday) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Optional<WeekdayReservationTimeMapping> weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
            photographerId, weekday);
        weekdayReservationTimeMapping.ifPresentOrElse(mapping ->
                mapping.updateReservationTimeFormatId(null),
            () -> new OwnershipNotFoundException("내가 소유한 요일 중, 해당 요일을 찾을 수 없습니다.")
        );
    }

    /*
     * Program은 수정을 하더라도 기존의 것을 soft delete하고 새로운 것을 생성하는 방식으로 진행한다.
     * 왜냐하면 기존의 Program을 예약한 고객이 있을 수 있기 때문이다.
     */
    @Override
    public photographerProgramIdResponse createProgram(
        ProgramRequest programRequest, Long programId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Photographer photographer = photographerRepository.getReferenceById(photographerId);
        Program program = programRequest.toEntity(photographer);
        if (programId != null) {
            softDeleteProgram(programId);
        }
        Program savedProgram = programRepository.save(program);

        return photographerProgramIdResponse.from(savedProgram);
    }

    @Override
    public PhotographerProgramListResponse getMyProgramList() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList()
        );
    }

    @Override
    public int softDeleteProgram(Long programId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        int deletedCount = programRepository.softDeleteByProgramIdAndPhotographerId(programId,
            photographerId);
        if (deletedCount == 0) {
            throw new OwnershipNotFoundException("내가 소유한 프로그램 중, 해당 프로그램을 찾을 수 없습니다.");
        }
        return deletedCount;
    }

    @Override
    public MonthReservationCheckListResponse getReservationListByMonth(LocalDate month) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        LocalDateTime startOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(), 1, 0,
            0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(),
            month.lengthOfMonth(), 23, 59, 59);

        List<Reservation> reservationList = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(
            photographerId, startOfMonth, endOfMonth);

        return MonthReservationCheckListResponse.from(
            reservationList.stream()
                .map(MonthReservationCheckResponse::from)
                .toList()
        );
    }

    @Override
    public PhotographerReservationInformationListResponse getReservationDetailListByDay(
        LocalDate day) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        LocalDateTime startOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 23, 59, 59);

        List<Reservation> reservationList = reservationRepository.findAllReservationWithEagerByPhotographerIdAndStartTimeBetween(
            photographerId, startOfDay, endOfDay);

        return PhotographerReservationInformationListResponse.from(
            reservationList.stream()
                .map(PhotographerReservationInformationResponse::from)
                .toList());
    }

    /*
     * 예약 상태를 변경한다.
     * 작가가 예약을 바꿀 수 있는 경우는 아래와 같다.
     * (PENDING -> APPROVED) (PENDING -> REJECTED) (APPROVED -> PHOTOGRAPHY_CANCELLED)
     */
    @Override
    public void changeReservationState(Long reservationId, ReservationState reservationState) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        reservationRepository.findReservationByIdAndPhotographerId(reservationId, photographerId)
            .ifPresentOrElse(reservation -> {
                if (isPossibleChangeReservationState(reservation, reservationState)) {
                    reservation.setReservationState(reservationState);
                } else {
                    throw new CanNotChangeReservationState("예약 상태를 변경할 수 없습니다.");
                }
            }, () -> {
                throw new OwnershipNotFoundException("내가 소유한 예약 중, 해당 예약을 찾을 수 없습니다.");
            });
    }

    private boolean isPossibleChangeReservationState(Reservation currentReservation,
        ReservationState targetReservationState) {
        return switch (currentReservation.getReservationState()) {
            case PENDING -> targetReservationState == ReservationState.APPROVED
                || targetReservationState == ReservationState.REJECTED;
            case APPROVED -> targetReservationState == ReservationState.PHOTOGRAPHY_CANCELLED;
            default -> false;
        };
    }
}
