package net.nodus.site

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface SiteRepository : MongoRepository<Site, String> {
    fun findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(userAccountId: String): List<Site>
    fun findByIdAndUserAccountIdAndDeletedAtIsNull(id: String, userAccountId: String): Site?
    fun findByDeletedAtBefore(deletedAt: Instant): List<Site>
}