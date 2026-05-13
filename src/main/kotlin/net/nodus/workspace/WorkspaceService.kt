package net.nodus.workspace

import net.nodus.config.exception.GlobalException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class WorkspaceService(
    private val workspaceRepository: WorkspaceRepository
) {
    fun create(userId: String, request: CreateWorkspaceRequest): Workspace =
        workspaceRepository.save(Workspace(userAccountId = userId, name = request.name))

    fun list(userId: String): List<Workspace> =
        workspaceRepository.findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)

    fun get(userId: String, workspaceId: String): Workspace =
        workspaceRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(workspaceId, userId)
            ?: throw GlobalException.DataNotFound("Workspace not found")

    fun update(userId: String, workspaceId: String, request: UpdateWorkspaceRequest): Workspace {
        val workspace = get(userId, workspaceId)

        request.name?.let { workspace.name = it }
        workspace.updatedAt = Instant.now()

        return workspaceRepository.save(workspace)
    }

    fun delete(userId: String, workspaceId: String) {
        val workspace = get(userId, workspaceId)
        workspace.deletedAt = Instant.now()
        workspace.updatedAt = Instant.now()
        workspaceRepository.save(workspace)
    }
}