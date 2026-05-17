package net.nodus.site.entity

import net.nodus.common.MutableDocument
import net.nodus.site.SiteKeyStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("site_keys")
@CompoundIndex(
    name = "site_active_key_unique",
    def = "{'siteId': 1, 'status': 1}",
    unique = true,
    partialFilter = "{'status': 'ACTIVE'}"
)
class SiteKey (
    @Id
    val id: String? = null,

    @Indexed
    val userAccountId: String,

    @Indexed
    val siteId: String,

    @Indexed
    val keyPrefix: String,

    val keyHash: String,

    @Indexed
    var status: SiteKeyStatus = SiteKeyStatus.ACTIVE,

    var revokedAt: Instant? = null,
) : MutableDocument()
