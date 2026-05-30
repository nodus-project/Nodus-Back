package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class NotImplemented extends GlobalException {

    public NotImplemented(String message) {
        super(message, ErrorCode.NOT_IMPLEMENTED);
    }
}
