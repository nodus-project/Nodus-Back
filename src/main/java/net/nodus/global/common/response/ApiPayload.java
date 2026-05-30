package net.nodus.global.common.response;

import net.nodus.global.common.exception.GlobalException;
import net.nodus.global.error.ErrorCode;

public record ApiPayload<T>(String msg, int errorCode, T payload) {

    public static ApiPayload<Void> success() {
        return new ApiPayload<>("Success", ErrorCode.SUCCESS.getCode(), null);
    }

    public static <T> ApiPayload<T> success(T payload) {
        return new ApiPayload<>("Success", ErrorCode.SUCCESS.getCode(), payload);
    }

    public static <T> ApiPayload<T> success(T payload, String message) {
        return new ApiPayload<>(message, ErrorCode.SUCCESS.getCode(), payload);
    }

    public static ApiPayload<Void> error(String msg, ErrorCode errorCode) {
        return new ApiPayload<>(msg, errorCode.getCode(), null);
    }

    public static ApiPayload<Void> error(GlobalException exception) {
        return new ApiPayload<>(exception.getMessage(), exception.getErrorCode().getCode(), null);
    }
}
