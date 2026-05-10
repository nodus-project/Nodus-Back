package net.nodus.site

data class SiteResponse(
    val id: String,
    val name: String,
    val domain: String?,
    val url: String?,
    val clientKey: String? = null
)

data class SiteKeyResponse(
    val keyId: String,
    val keyPrefix: String,
    val clientKey: String? = null,
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
