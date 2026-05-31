package net.nodus.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(0),
    INVALID_INPUT(1000),
    MISSING_PARAMETER(1001),
    INVALID_TYPE(1002),
    UNAUTHORIZED(1003),
    FORBIDDEN(1004),
    NOT_FOUND(2000),
    DUPLICATED(2001),
    DATABASE_ERROR(2002),
    DATA_EXPIRED(2003),
    DATA_INTEGRITY_VIOLATION(2004),
    INTERNAL_SERVER_ERROR(9000),
    EXTERNAL_API_FAILED(9001),
    NOT_IMPLEMENTED(9002),
    SERVICE_UNAVAILABLE(9003);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
