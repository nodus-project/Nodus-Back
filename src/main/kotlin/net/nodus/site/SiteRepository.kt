package net.nodus.site

import net.nodus.site.entity.Site
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface SiteRepository : MongoRepository<Site, String> {
    fun findByUserAccountIdAndWorkspaceIdAndDeletedAtIsNullOrderByCreatedAtDesc(
        userAccountId: String,
        workspaceId: String,
    ): List<Site>
    fun findByIdAndUserAccountIdAndDeletedAtIsNull(id: String, userAccountId: String): Site?
    fun findByWorkspaceIdAndDeletedAtIsNull(workspaceId: String): List<Site>
    fun findByDeletedAtBefore(deletedAt: Instant): List<Site>
}