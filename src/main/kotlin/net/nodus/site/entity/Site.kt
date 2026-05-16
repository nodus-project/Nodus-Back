package net.nodus.site.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("sites")
class Site (
    @Id val id: String? = null,
    @Indexed val userAccountId: String,
    @Indexed val workspaceId: String,
    var name: String,
    var domain: String? = null,
    var url: String? = null,
    @Indexed var deletedAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)