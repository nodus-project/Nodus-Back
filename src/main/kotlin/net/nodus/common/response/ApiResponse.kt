package net.nodus.common.response

import net.nodus.common.exception.GlobalException
import net.nodus.global.error.ErrorCode

data class ApiResponse<T> (
    val msg: String? = null,
    val errorCode: Int,
    val payload: T?
) {
    companion object {
        fun success(): ApiResponse<Void> {
            return ApiResponse(msg = "Success", errorCode = ErrorCode.SUCCESS.code, payload = null)
        }
        fun <T> success(payload: T?): ApiResponse<T> {
            return ApiResponse(msg = "Success", errorCode = ErrorCode.SUCCESS.code, payload = payload)
        }

        fun <T> success(payload: T?, message: String): ApiResponse<T> {
            return ApiResponse(msg = message, errorCode = ErrorCode.SUCCESS.code, payload = payload)
        }

        fun error(msg: String, errorCode: ErrorCode): ApiResponse<Void> {
            return ApiResponse(msg = msg, errorCode = errorCode.code, payload = null)
        }

        fun error(exception: GlobalException): ApiResponse<Void> {
            return ApiResponse(msg = exception.message, errorCode = exception.errorCode.code, payload = null)
        }
    }
}