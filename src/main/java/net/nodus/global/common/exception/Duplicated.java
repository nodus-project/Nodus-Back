package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class Duplicated extends GlobalException {

    public Duplicated(String message) {
        super(message, ErrorCode.DUPLICATED);
    }
}
