package net.nodus.site

import net.nodus.config.exception.GlobalException
import net.nodus.project.ProjectRepository
import net.nodus.site.dto.CreateSiteRequest
import net.nodus.site.dto.IssuedSiteKey
import net.nodus.site.dto.SiteCreateResult
import net.nodus.site.dto.UpdateSiteRequest
import net.nodus.site.entity.Site
import net.nodus.site.entity.SiteKey
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteKeyRepository: SiteKeyRepository,
    private val siteKeyService: SiteKeyService,
    private val projectRepository: ProjectRepository,
) {

    fun create(userId: String, request: CreateSiteRequest): SiteCreateResult {
        val project = projectRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(
            id = request.projectId,
            userAccountId = userId,
        ) ?: throw GlobalException.DataNotFound("Project not found.")

        val site = siteRepository.save(
            Site(
                userAccountId = userId,
                workspaceId = project.workspaceId,
                projectId = requireNotNull(project.id),
                name = request.name,
                domain = request.domain,
                url = request.url,
            )
        )

        return SiteCreateResult(site = site, issuedKey = siteKeyService.issue(site))
    }

    fun list(userId: String, projectId: String): List<Site> =
        siteRepository.findByUserAccountIdAndProjectIdAndDeletedAtIsNullOrderByCreatedAtDesc(
            userAccountId = userId,
            projectId = projectId
        )

    fun get(userId: String, siteId: String): Site =
        siteRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(siteId, userId)
            ?: throw GlobalException.DataNotFound("Site not found")

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
            ?: throw GlobalException.DataNotFound("Active site key not found")
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