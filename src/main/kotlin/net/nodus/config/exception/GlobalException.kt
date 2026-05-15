package net.nodus.config.exception

import net.nodus.global.error.ErrorCode

sealed class GlobalException(
    val msg: String,
    val errorCode: ErrorCode,
) : RuntimeException(msg) {

    class DataNotFound(
        message: String = "데이터를 찾을 수 없습니다.",
        errorCode: ErrorCode = ErrorCode.NOT_FOUND,
    ) : GlobalException(message, errorCode)

    class ServerError(
        message: String = "서버 내부 오류입니다.",
        errorCode: ErrorCode = ErrorCode.INTERNAL_SERVER_ERROR,
    ) : GlobalException(message, errorCode)

    class InvalidInput(
        message: String = "입력 값이 올바르지 않습니다.",
        errorCode: ErrorCode = ErrorCode.INVALID_INPUT,
    ) : GlobalException(message, errorCode)

    class Unauthorized(
        message: String = "인증이 필요합니다.",
        errorCode: ErrorCode = ErrorCode.UNAUTHORIZED,
    ) : GlobalException(message, errorCode)

    class Forbidden(
        message: String = "접근 권한이 없습니다.",
        errorCode: ErrorCode = ErrorCode.FORBIDDEN,
    ) : GlobalException(message, errorCode)

    class Duplicated(
        message: String = "이미 존재하는 엔티티입니다.",
        errorCode: ErrorCode = ErrorCode.DUPLICATED,
    ): GlobalException(message, errorCode)

    class DataExpired(
        message: String = "만료된 데이터입니다.",
        errorCode: ErrorCode = ErrorCode.DATA_EXPIRED,
    ): GlobalException(message, errorCode)

    class DataIntegrityViolation(
        message: String = "데이터 제약 조건을 위반했습니다.",
        errorCode: ErrorCode = ErrorCode.DATA_INTEGRITY_VIOLATION,
    ): GlobalException(message, errorCode)

    class ExternalApiFailed(
        message: String = "외부 API 호출에 실패했습니다.",
        errorCode: ErrorCode = ErrorCode.EXTERNAL_API_FAILED,
    ): GlobalException(message, errorCode)

    class NotImplemented(
        message: String = "구현되지 않은 기능입니다.",
        errorCode: ErrorCode = ErrorCode.NOT_IMPLEMENTED,
    ): GlobalException(message, errorCode)

    class ServiceUnavailable(
        message: String = "서비스를 이용할 수 없습니다.",
        errorCode: ErrorCode = ErrorCode.SERVICE_UNAVAILABLE,
    ): GlobalException(message, errorCode)
}