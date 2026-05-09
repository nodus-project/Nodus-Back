package net.nodus.sessionlog.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import net.nodus.config.ApiResponse
import net.nodus.sessionlog.service.SessionLogService
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
        val sessionLog = sessionLogService.create(
            clientKey = request.clientKey,
            userSession = request.userSession,
        )

        val result = SessionLogResponse(
            id = requireNotNull(sessionLog.id),
            clientKeyId = sessionLog.clientKeyId,
            userSession = sessionLog.userSession,
            createdAt = sessionLog.createdAt.toString()
        )

        return ApiResponse.success(result)
    }
}

data class CreateSessionLogRequest(
    @field:NotBlank
    val clientKey: String,

    @field:NotBlank
    val userSession: String,
)

data class SessionLogResponse(
    val id: String,
    val clientKeyId: String,
    val userSession: String,
    val createdAt: String,
)
