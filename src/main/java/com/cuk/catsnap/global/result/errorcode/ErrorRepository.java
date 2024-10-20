package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.Exception.BusinessException;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberIdException;
import com.cuk.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import com.cuk.catsnap.global.result.ResultCode;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ErrorRepository {
    private final Map<Class<? extends BusinessException>, ResultCode> errorMap = new ConcurrentHashMap<>();

    ErrorRepository() {
        errorMap.put(DuplicatedMemberIdException.class, MemberErrorCode.DUPLICATED_SIGNUP_ID);
        errorMap.put(DuplicatedPhotographerException.class, PhotographerErrorCode.DUPLICATED_SIGNUP_ID);
        errorMap.put(OwnershipNotFoundException.class, OwnershipErrorCode.NOT_FOUND_OWNERSHIP);
    }

    public ResultCode getResultCode(BusinessException e){
        return errorMap.get(e.getClass());
    }
}
