package net.nodus.site

import net.nodus.site.entity.Site
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface SiteRepository : MongoRepository<Site, String> {
    fun findByUserAccountIdAndProjectIdAndDeletedAtIsNullOrderByCreatedAtDesc(
        userAccountId: String,
        projectId: String,
    ): List<Site>
    fun findByIdAndUserAccountIdAndDeletedAtIsNull(id: String, userAccountId: String): Site?

    fun findByProjectIdAndDeletedAtIsNull(projectId: String): List<Site>
    fun findByDeletedAtBefore(deletedAt: Instant): List<Site>
}