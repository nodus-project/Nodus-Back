package net.nodus.workspace

import jakarta.validation.constraints.NotBlank

data class CreateWorkspaceRequest(
    @field:NotBlank
    val name: String
)

data class UpdateWorkspaceRequest(
    val name: String? = null
)

data class WorkspaceResponse(
    val id: String,
    val name: String,
)

fun Workspace.toResponse(): WorkspaceResponse =
    WorkspaceResponse(
        id = requireNotNull(id),
        name = name
    )