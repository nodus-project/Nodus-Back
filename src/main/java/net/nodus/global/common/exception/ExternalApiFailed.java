package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class ExternalApiFailed extends GlobalException {

    public ExternalApiFailed(String message) {
        super(message, ErrorCode.EXTERNAL_API_FAILED);
    }
}
