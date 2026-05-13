package net.nodus.workspace

import jakarta.validation.Valid
import net.nodus.config.ApiResponse
import net.nodus.security.AuthUserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workspaces")
class WorkspaceController(
    private val workspaceService: WorkspaceService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateWorkspaceRequest,
    ): ApiResponse<WorkspaceResponse> =
        ApiResponse.success(workspaceService.create(user.id, request).toResponse())

    @GetMapping
    fun list(@AuthenticationPrincipal user: AuthUserPrincipal): ApiResponse<List<WorkspaceResponse>> =
        ApiResponse.success(workspaceService.list(user.id).map { it.toResponse() })

    @PostMapping("/{workspaceId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable workspaceId: String,
        @Valid @RequestBody request: UpdateWorkspaceRequest,
    ): ApiResponse<WorkspaceResponse> =
        ApiResponse.success(workspaceService.update(user.id, workspaceId, request).toResponse())

    @DeleteMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@AuthenticationPrincipal user: AuthUserPrincipal, @PathVariable workspaceId: String): ApiResponse<Void> {
        workspaceService.delete(user.id, workspaceId)
        return ApiResponse.success()
    }
}