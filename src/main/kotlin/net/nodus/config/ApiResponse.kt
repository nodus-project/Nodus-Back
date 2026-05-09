package net.nodus.config

import net.nodus.config.exception.GlobalException
import net.nodus.global.error.ErrorCode

data class ApiResponse<T> (
    val msg: String? = null,
    val errorCode: ErrorCode,
    val payload: T?
) {
    companion object {
        fun success(): ApiResponse<Void> {
            return ApiResponse(msg = "Success", errorCode = ErrorCode.SUCCESS, payload = null)
        }
        fun <T> success(payload: T?): ApiResponse<T> {
            return ApiResponse(msg = "Success", errorCode = ErrorCode.SUCCESS, payload = payload)
        }

        fun <T> success(payload: T?, message: String): ApiResponse<T> {
            return ApiResponse(msg = message, errorCode = ErrorCode.SUCCESS, payload = payload)
        }

        fun error(msg: String, errorCode: ErrorCode): ApiResponse<Void> {
            return ApiResponse(msg = msg, errorCode = errorCode, payload = null)
        }

        fun error(exception: GlobalException): ApiResponse<Void> {
            return ApiResponse(msg = exception.message, errorCode = exception.errorCode, payload = null)
        }
    }
}