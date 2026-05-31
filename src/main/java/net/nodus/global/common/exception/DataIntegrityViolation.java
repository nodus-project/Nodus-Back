package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class DataIntegrityViolation extends GlobalException {

    public DataIntegrityViolation(String message) {
        super(message, ErrorCode.DATA_INTEGRITY_VIOLATION);
    }
}
