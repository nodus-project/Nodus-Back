package net.nodus.global.common.exception;

import net.nodus.global.error.ErrorCode;

public class ServerError extends GlobalException {

    public ServerError(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
