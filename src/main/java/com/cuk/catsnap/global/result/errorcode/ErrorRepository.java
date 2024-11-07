package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.Exception.BusinessException;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberIdException;
import com.cuk.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import com.cuk.catsnap.global.Exception.reservation.CanNotChangeReservationState;
import com.cuk.catsnap.global.Exception.reservation.CanNotReserveAfterDeadline;
import com.cuk.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import com.cuk.catsnap.global.Exception.reservation.DeletedProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundProgramException;
import com.cuk.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import com.cuk.catsnap.global.Exception.reservation.OverLappingTimeException;
import com.cuk.catsnap.global.result.ResultCode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ErrorRepository {

    private final Map<Class<? extends BusinessException>, ResultCode> errorMap = new ConcurrentHashMap<>();

    ErrorRepository() {
        errorMap.put(DuplicatedMemberIdException.class, MemberErrorCode.DUPLICATED_SIGNUP_ID);
        errorMap.put(DuplicatedPhotographerException.class,
            PhotographerErrorCode.DUPLICATED_SIGNUP_ID);
        errorMap.put(OwnershipNotFoundException.class, OwnershipErrorCode.NOT_FOUND_OWNERSHIP);
        errorMap.put(CanNotChangeReservationState.class, OwnershipErrorCode.NOT_FOUND_OWNERSHIP);
        errorMap.put(NotFoundProgramException.class, ReservationErrorCode.NOT_FOUND_PROGRAM);
        errorMap.put(DeletedProgramException.class, ReservationErrorCode.DELETED_PROGRAM);
        errorMap.put(NotFoundStartTimeException.class, ReservationErrorCode.NOT_FOUND_START_TIME);
        errorMap.put(OverLappingTimeException.class,
            ReservationErrorCode.CANNOT_RESERVATION_OVERBOOKING);
        errorMap.put(CanNotStartTimeBeforeNow.class,
            ReservationErrorCode.CANNOT_RESERVATION_BEFORE_NOW);
        errorMap.put(CanNotReserveAfterDeadline.class,
            ReservationErrorCode.CANNOT_RESERVATION_AFTER_DEADLINE);
    }

    public ResultCode getResultCode(BusinessException e) {
        return errorMap.get(e.getClass());
    }
}
