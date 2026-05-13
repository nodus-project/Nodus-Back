package net.nodus.config

import net.nodus.project.ProjectRepository
import net.nodus.site.SiteRepository
import net.nodus.workspace.WorkspaceRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class DeletedDataCleanupService(
    private val siteRepository: SiteRepository,
    private val projectRepository: ProjectRepository,
    private val workspaceRepository: WorkspaceRepository,
) {
    fun hardDeleteExpiredData() {
        val threshold = Instant.now().minus(365, ChronoUnit.DAYS)

        siteRepository.deleteAll(siteRepository.findByDeletedAtBefore(threshold))
        projectRepository.deleteAll(projectRepository.findByDeletedAtBefore(threshold))
        workspaceRepository.deleteAll(workspaceRepository.findByDeletedAtBefore(threshold))
    }
}