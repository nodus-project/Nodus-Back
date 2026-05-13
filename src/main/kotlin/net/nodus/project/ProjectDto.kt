package net.nodus.project

import jakarta.validation.constraints.NotBlank

data class CreateProjectRequest(
    @field:NotBlank
    val workspaceId: String,

    @field:NotBlank
    val name: String,
)

data class UpdateProjectRequest(
    val name: String? = null,
)

data class ProjectResponse(
    val id: String,
    val workspaceId: String,
    val name: String,
)

fun Project.toResponse(): ProjectResponse =
    ProjectResponse(
        id = requireNotNull(id),
        workspaceId = workspaceId,
        name = name
    )