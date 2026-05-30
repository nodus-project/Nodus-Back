package net.nodus.global.common.exception;

import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GlobalException.class)
    public ApiPayload<Void> handleGlobalException(GlobalException ex) {
        log.warn(ex.getMessage());
        return ApiPayload.error(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiPayload<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String firstErrorMessage = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getDefaultMessage() == null ? "Invalid input."
                : error.getDefaultMessage())
            .orElse("Invalid input.");
        log.warn("Validation Failed: {}", firstErrorMessage);
        return ApiPayload.error(firstErrorMessage, ErrorCode.MISSING_PARAMETER);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiPayload<Void> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn("Missing Parameter: {}", ex.getParameterName());
        return ApiPayload.error(ex.getParameterName() + " parameter is missing.",
            ErrorCode.MISSING_PARAMETER);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiPayload<Void> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type Mismatch: {}", ex.getName());
        return ApiPayload.error("Invalid input type.", ErrorCode.INVALID_TYPE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiPayload<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ApiPayload.error("HTTP method is not supported.", ErrorCode.INVALID_INPUT);
    }

    @ExceptionHandler(Exception.class)
    public ApiPayload<Void> handleAllException(Exception ex) {
        log.error("Unexpected Server Error: {}", ex.getMessage(), ex);
        return ApiPayload.error("Internal server error.", ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
