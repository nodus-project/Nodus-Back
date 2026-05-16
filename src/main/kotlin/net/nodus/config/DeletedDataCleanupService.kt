package net.nodus.config

import net.nodus.site.SiteRepository
import net.nodus.workspace.WorkspaceRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class DeletedDataCleanupService(
    private val siteRepository: SiteRepository,
    private val workspaceRepository: WorkspaceRepository,
) {
    fun hardDeleteExpiredData() {
        val threshold = Instant.now().minus(365, ChronoUnit.DAYS)

        siteRepository.deleteAll(siteRepository.findByDeletedAtBefore(threshold))
        workspaceRepository.deleteAll(workspaceRepository.findByDeletedAtBefore(threshold))
    }
}