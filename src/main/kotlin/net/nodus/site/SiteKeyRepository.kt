package net.nodus.site

import net.nodus.site.entity.SiteKey
import org.springframework.data.mongodb.repository.MongoRepository

interface SiteKeyRepository : MongoRepository<SiteKey, String> {
    fun findBySiteIdAndStatus(siteId: String, status: SiteKeyStatus): SiteKey?
    fun findAllByKeyPrefixAndStatus(keyPrefix: String, status: SiteKeyStatus): List<SiteKey>
}