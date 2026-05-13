package net.nodus.project

import net.nodus.config.exception.GlobalException
import net.nodus.workspace.WorkspaceService
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val workspaceService: WorkspaceService
) {
    fun create(userId: String, request: CreateProjectRequest): Project {
        val workspace = workspaceService.get(userId, request.workspaceId)
        return projectRepository.save(
            Project(userAccountId = userId, workspaceId = workspace.id!!, name = request.name))
    }

    fun list(userId: String, workspaceId: String): List<Project> {
        workspaceService.get(userId, workspaceId)
        return projectRepository.findByUserAccountIdAndWorkspaceIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, workspaceId)
    }

    fun get(userId: String, projectId: String): Project =
        projectRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(projectId, userId)
            ?: throw GlobalException.DataNotFound("Project not found")

    fun update(userId: String, projectId: String, request: UpdateProjectRequest): Project {
        val project = get(userId, projectId)
        request.name?.let { project.name = it }
        project.updatedAt = Instant.now()
        return projectRepository.save(project)
    }

    fun delete(userId: String, projectId: String) {
        val project = get(userId, projectId)
        project.deletedAt = Instant.now()
        project.updatedAt = Instant.now()
        projectRepository.save(project)
    }
}