package net.nodus.sessionlog

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import net.nodus.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "SessionLog", description = "세션 로그 관리 API")
@RestController
@RequestMapping("/session-logs")
class SessionLogController(
    private val sessionLogService: SessionLogService,
) {

    @Operation(
        summary = "세션 로그 생성",
        description = "Bearer 인증 없이 요청 본문의 siteKey를 검증하여 사용자 세션 로그를 저장합니다."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateSessionLogRequest): ApiResponse<SessionLogResponse> {
        val sessionLog = sessionLogService.create(request.siteKey, request.userSession)

       return ApiResponse.success(sessionLog.toResponse())
    }
}