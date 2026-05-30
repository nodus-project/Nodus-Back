package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class InvalidInput extends GlobalException {

    public InvalidInput(String message) {
        super(message, ErrorCode.INVALID_INPUT);
    }
}
