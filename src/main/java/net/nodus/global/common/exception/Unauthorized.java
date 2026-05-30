package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class Unauthorized extends GlobalException {

    public Unauthorized(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
