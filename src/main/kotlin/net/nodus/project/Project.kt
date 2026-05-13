package net.nodus.project

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("projects")
class Project(
    @Id val id: String? = null,
    @Indexed val userAccountId: String,
    @Indexed val workspaceId: String,
    var name: String,
    @Indexed var deletedAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)