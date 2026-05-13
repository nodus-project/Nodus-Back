package net.nodus.sessionlog

import jakarta.validation.constraints.NotBlank

data class CreateSessionLogRequest(
    @field:NotBlank
    val clientKey: String,

    @field:NotBlank
    val userSession: String,
)

data class SessionLogResponse(
    val id: String,
    val clientKeyId: String,
    val userAccountId: String,
    val workspaceId: String,
    val projectId: String,
    val siteId: String,
    val userSession: String,
    val createdAt: String,
)

fun SessionLog.toResponse(): SessionLogResponse =
    SessionLogResponse(
        id = requireNotNull(id),
        clientKeyId = clientKeyId,
        userAccountId = userAccountId,
        workspaceId = workspaceId,
        projectId = projectId,
        siteId = siteId,
        userSession = userSession,
        createdAt = createdAt.toString(),
    )