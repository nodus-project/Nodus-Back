package net.nodus.site

import net.nodus.site.entity.SiteKey
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface SiteKeyRepository : MongoRepository<SiteKey, String> {
    fun findBySiteIdAndStatusAndDeletedAtIsNull(siteId: String, status: SiteKeyStatus): SiteKey?
    fun findAllByKeyPrefixAndStatusAndDeletedAtIsNull(keyPrefix: String, status: SiteKeyStatus): List<SiteKey>

    fun findByDeletedAtBefore(deletedAt: Instant): List<SiteKey>
}