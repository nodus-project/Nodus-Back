package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class Forbidden extends GlobalException {

    public Forbidden(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}
