package net.nodus.sessionlog

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import net.nodus.config.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/session-logs")
class SessionLogController(
    private val sessionLogService: SessionLogService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateSessionLogRequest): ApiResponse<SessionLogResponse> {
        val sessionLog = sessionLogService.create(request.siteKey, request.userSession)

       return ApiResponse.success(sessionLog.toResponse())
    }
}