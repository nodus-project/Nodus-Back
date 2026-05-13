package net.nodus.workspace

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface WorkspaceRepository : MongoRepository<Workspace, String> {
    fun findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(userAccountId: String): List<Workspace>
    fun findByIdAndUserAccountIdAndDeletedAtIsNull(id: String, userAccountId: String): Workspace?
    fun findByDeletedAtBefore(deletedAt: Instant): List<Workspace>
}