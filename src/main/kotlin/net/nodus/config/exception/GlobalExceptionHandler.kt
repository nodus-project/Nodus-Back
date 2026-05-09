package net.nodus.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import net.nodus.config.ApiResponse
import net.nodus.global.error.ErrorCode
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(ex: GlobalException): ApiResponse<Void> {
        logger.warn { ex.message }
        return ApiResponse.error(ex)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ApiResponse<Void> {
        val firstErrorMessage = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "입력값이 올바르지 않습니다."
        logger.warn { "Validation Failed: $firstErrorMessage" }
        return ApiResponse.error(
            msg = firstErrorMessage,
            errorCode = ErrorCode.MISSING_PARAMETER
        )
    }

    // 3. 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParams(ex: MissingServletRequestParameterException): ApiResponse<Void> {
        logger.warn { "Missing Parameter: ${ex.parameterName}" }
        return ApiResponse.error(
            msg = "${ex.parameterName} 파라미터가 누락되었습니다.",
            errorCode = ErrorCode.MISSING_PARAMETER
        )
    }

    // 4. 타입 불일치 (예: 숫자가 필요한데 문자를 보냄)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ApiResponse<Void> {
        logger.warn { "Type Mismatch: ${ex.name}" }
        return ApiResponse.error(
            msg = "입력값의 타입이 올바르지 않습니다.",
            errorCode = ErrorCode.INVALID_TYPE
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(ex: HttpRequestMethodNotSupportedException): ApiResponse<Void> {
        return ApiResponse.error(
            msg ="지원하지 않는 HTTP 메서드입니다.",
            errorCode = ErrorCode.INVALID_INPUT
        )
    }

    // 6. 최후의 보루: 그 외 모든 예외
    @ExceptionHandler(Exception::class)
    fun handleAllException(ex: Exception): ApiResponse<Void> {
        logger.error(ex) { "Unexpected Server Error: ${ex.message}" }
        return ApiResponse.error(
            msg = "서버 내부 오류가 발생했습니다.",
            errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        )
    }
}