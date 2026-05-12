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

}