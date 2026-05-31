package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class DataExpired extends GlobalException {

    public DataExpired(String message) {
        super(message, ErrorCode.DATA_EXPIRED);
    }
}
