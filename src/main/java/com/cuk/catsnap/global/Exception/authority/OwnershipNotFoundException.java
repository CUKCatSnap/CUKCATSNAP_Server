package com.cuk.catsnap.global.Exception.authority;

import com.cuk.catsnap.global.Exception.BusinessException;

/*
 * 자신이 소유한 리소를 수정할 때, 리소스의 id로 검색을 합니다.
 * 이때 해당 리소스가 내것이 아니라면 발생하는 예외입니다.
 * 내부적으로 해당 리소스가 자신의 소유인지와 리소스의 id를 검색하는 로직이 한번에 발생하기 때문에
 * 이 둘을 구분하지 않고 하나의 예외로 처리합니다.
 */
public class OwnershipNotFoundException extends BusinessException {

    public OwnershipNotFoundException(String message) {
        super(message);
    }

    public OwnershipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OwnershipNotFoundException(Throwable cause) {
        super(cause);
    }
}
