package net.nodus.global.common.exception;

import lombok.Getter;
import net.nodus.global.error.ErrorCode;

@Getter
public abstract class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    protected GlobalException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
