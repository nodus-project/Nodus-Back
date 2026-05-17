package net.nodus.sessionlog

import jakarta.validation.constraints.NotBlank

data class CreateSessionLogRequest(
    @field:NotBlank
    val siteKey: String,

    @field:NotBlank
    val userSession: String,
)

data class SessionLogResponse(
    val id: String,
    val siteKeyId: String,
    val userAccountId: String,
    val siteId: String,
    val userSession: String,
    val createdAt: String,
)

fun SessionLog.toResponse(): SessionLogResponse =
    SessionLogResponse(
        id = requireNotNull(id),
        siteKeyId = siteKeyId,
        userAccountId = userAccountId,
        siteId = siteId,
        userSession = userSession,
        createdAt = createdAt.toString(),
    )
