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

    public static class DataNotFound extends GlobalException {

        public DataNotFound(String message) {
            super(message, ErrorCode.NOT_FOUND);
        }
    }

    public static class ServerError extends GlobalException {

        public ServerError(String message) {
            super(message, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static class InvalidInput extends GlobalException {

        public InvalidInput(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }

    public static class Unauthorized extends GlobalException {

        public Unauthorized(String message) {
            super(message, ErrorCode.UNAUTHORIZED);
        }
    }

    public static class Forbidden extends GlobalException {

        public Forbidden(String message) {
            super(message, ErrorCode.FORBIDDEN);
        }
    }

    public static class Duplicated extends GlobalException {

        public Duplicated(String message) {
            super(message, ErrorCode.DUPLICATED);
        }
    }

    public static class DataExpired extends GlobalException {

        public DataExpired(String message) {
            super(message, ErrorCode.DATA_EXPIRED);
        }
    }

    public static class DataIntegrityViolation extends GlobalException {

        public DataIntegrityViolation(String message) {
            super(message, ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
    }

    public static class ExternalApiFailed extends GlobalException {

        public ExternalApiFailed(String message) {
            super(message, ErrorCode.EXTERNAL_API_FAILED);
        }
    }

    public static class NotImplemented extends GlobalException {

        public NotImplemented(String message) {
            super(message, ErrorCode.NOT_IMPLEMENTED);
        }
    }

    public static class ServiceUnavailable extends GlobalException {

        public ServiceUnavailable(String message) {
            super(message, ErrorCode.SERVICE_UNAVAILABLE);
        }
    }
}
