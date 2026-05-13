package net.nodus.project

import jakarta.validation.Valid
import net.nodus.config.ApiResponse
import net.nodus.security.AuthUserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateProjectRequest
    ): ApiResponse<ProjectResponse> =
        ApiResponse.success(projectService.create(user.id, request).toResponse())

    @GetMapping
    fun list(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @RequestBody workspaceId: String,
    ): ApiResponse<List<ProjectResponse>> =
        ApiResponse.success(projectService.list(user.id, workspaceId).map { it.toResponse() })

    @PatchMapping("/{projectId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable projectId: String,
        @RequestBody request: UpdateProjectRequest,
    ): ApiResponse<ProjectResponse> =
        ApiResponse.success(projectService.update(user.id, projectId, request).toResponse())

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable projectId: String
    ): ApiResponse<Void> {
        projectService.delete(user.id, projectId)
        return ApiResponse.success()
    }
}