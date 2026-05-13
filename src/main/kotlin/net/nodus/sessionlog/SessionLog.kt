package net.nodus.sessionlog

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "session_logs")
class SessionLog (
    @Id
    val id: String? = null,

    @Indexed
    val clientKeyId: String,

    @Indexed
    val workspaceId: String,

    @Indexed
    val projectId: String,

    @Indexed
    val siteId: String,

    @Indexed
    val userAccountId: String,

    val userSession: String,

    val createdAt: Instant = Instant.now(),
)