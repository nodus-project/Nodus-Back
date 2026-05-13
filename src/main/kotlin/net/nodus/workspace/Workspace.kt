package net.nodus.workspace

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("workspaces")
class Workspace (
    @Id val id: String? = null,
    @Indexed val userAccountId: String,
    var name: String,
    @Indexed var deletedAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)