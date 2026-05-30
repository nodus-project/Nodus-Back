package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class ServiceUnavailable extends GlobalException {

    public ServiceUnavailable(String message) {
        super(message, ErrorCode.SERVICE_UNAVAILABLE);
    }
}
