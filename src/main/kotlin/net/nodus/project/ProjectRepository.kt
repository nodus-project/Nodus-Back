package net.nodus.project

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant


interface ProjectRepository : MongoRepository<Project, String> {
    fun findByUserAccountIdAndWorkspaceIdAndDeletedAtIsNullOrderByCreatedAtDesc(
        userAccountId: String,
        workspaceId: String,
    ): List<Project>

    fun findByIdAndUserAccountIdAndDeletedAtIsNull(id: String, userAccountId: String): Project?
    fun findByWorkspaceIdAndDeletedAtIsNull(workspaceId: String): List<Project>
    fun findByDeletedAtBefore(deletedAt: Instant): List<Project>
}