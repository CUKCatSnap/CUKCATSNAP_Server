package net.catsnap.global.result.errorcode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.catsnap.global.Exception.BusinessException;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.Exception.member.DuplicatedMemberIdException;
import net.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import net.catsnap.global.Exception.reservation.CanNotChangeReservationState;
import net.catsnap.global.Exception.reservation.CanNotReserveAfterDeadline;
import net.catsnap.global.Exception.reservation.CanNotStartTimeBeforeNow;
import net.catsnap.global.Exception.reservation.DeletedProgramException;
import net.catsnap.global.Exception.reservation.NotFoundProgramException;
import net.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import net.catsnap.global.Exception.reservation.OverLappingTimeException;
import net.catsnap.global.result.ResultCode;
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
        errorMap.put(ResourceNotFoundException.class, OwnershipErrorCode.NOT_FOUND_RESOURCE);
    }

    public ResultCode getResultCode(BusinessException e) {
        return errorMap.get(e.getClass());
    }
}
