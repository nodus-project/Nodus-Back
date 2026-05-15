package net.nodus.site.dto

import net.nodus.site.SiteKeyStatus
import net.nodus.site.entity.Site
import net.nodus.site.entity.SiteKey

data class SiteResponse(
    val id: String,
    val name: String,
    val domain: String?,
    val url: String?,
    val siteKey: String? = null
)

data class SiteKeyResponse(
    val keyId: String,
    val keyPrefix: String,
    val siteKey: String? = null,
    val status: SiteKeyStatus,
)

data class SiteCreateResult(
    val site: Site,
    val issuedKey: IssuedSiteKey,
)

data class IssuedSiteKey(
    val siteKey: SiteKey,
    val rawKey: String,
)

fun Site.toResponse(): SiteResponse =
    SiteResponse(
        id = requireNotNull(id),
        name = name,
        domain = domain,
        url = url,
    )

fun SiteKey.toResponse(): SiteKeyResponse =
    SiteKeyResponse(
        keyId = requireNotNull(id),
        keyPrefix = keyPrefix,
        status = status,
    )