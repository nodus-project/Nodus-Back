package net.nodus.site

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteKeyRepository: SiteKeyRepository,
    private val siteKeyService: SiteKeyService,
) {

    fun create(userId: String, request: CreateSiteRequest): SiteCreateResult {
        val site = siteRepository.save(
            Site(
                userAccountId = userId,
                name = request.name,
                domain = request.domain,
                url = request.url
            )
        )

        return SiteCreateResult(
            site = site,
            issuedKey = siteKeyService.issue(site)
        )
    }

    fun list(userId: String): List<Site> =
        siteRepository.findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)

    fun get(userId: String, siteId: String): Site =
        siteRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(siteId, userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found")

    fun update(userId: String, siteId: String, request: UpdateSiteRequest): Site {
        val site = get(userId, siteId)

        request.name?.let { site.name = it }
        site.domain = request.domain
        site.url = request.url
        site.updatedAt = Instant.now()

        return siteRepository.save(site)
    }

    fun delete(userId: String, siteId: String) {
        val site = get(userId, siteId)

        site.deletedAt = Instant.now()
        site.updatedAt = Instant.now()

        siteRepository.save(site)
        siteKeyService.revokeActive(siteId)
    }

    fun keyInfo(userId: String, siteId: String): SiteKey {
        get(userId, siteId)

        return siteKeyRepository.findBySiteIdAndStatus(siteId, SiteKeyStatus.ACTIVE)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Active site key not found")
    }

    fun rotateKey(userId: String, siteId: String): IssuedSiteKey {
        val site = get(userId, siteId)
        return siteKeyService.issue(site)
    }

    fun revokeKey(userId: String, siteId: String) {
        get(userId, siteId)
        siteKeyService.revokeActive(siteId)
    }
}