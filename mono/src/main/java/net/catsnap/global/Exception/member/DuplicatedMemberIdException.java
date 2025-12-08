package net.catsnap.global.Exception.member;

import net.catsnap.global.Exception.BusinessException;

public class DuplicatedMemberIdException extends BusinessException {

    public DuplicatedMemberIdException(String message) {
        super(message);
    }

    public DuplicatedMemberIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedMemberIdException(Throwable cause) {
        super(cause);
    }
}
