package net.nodus.workspace

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "Workspace", description = "워크스페이스 관리 API")
@SecurityRequirement(name = "bearerToken")
@RestController
@RequestMapping("/workspaces")
class WorkspaceController(
    private val workspaceService: WorkspaceService
) {
    @Operation(summary = "워크스페이스 생성", description = "워크스페이스를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateWorkspaceRequest,
    ): ApiResponse<WorkspaceResponse> =
        ApiResponse.success(workspaceService.create(user.id, request).toResponse())

    @Operation(summary = "워크스페이스 목록 조회", description = "로그인한 사용자가 소유한 워크스페이스 목록을 조회합니다.")
    @GetMapping
    fun list(@AuthenticationPrincipal user: AuthUserPrincipal): ApiResponse<List<WorkspaceResponse>> =
        ApiResponse.success(workspaceService.list(user.id).map { it.toResponse() })

    @Operation(summary = "워크스페이스 수정", description = "워크스페이스 이름 등 수정 가능한 정보를 변경합니다.")
    @PostMapping("/{workspaceId}")
    fun update(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable workspaceId: String,
        @Valid @RequestBody request: UpdateWorkspaceRequest,
    ): ApiResponse<WorkspaceResponse> =
        ApiResponse.success(workspaceService.update(user.id, workspaceId, request).toResponse())

    @Operation(summary = "워크스페이스 삭제", description = "워크스페이스를 삭제합니다.")
    @DeleteMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@Parameter(hidden = true) @AuthenticationPrincipal user: AuthUserPrincipal, @PathVariable workspaceId: String): ApiResponse<Void> {
        workspaceService.delete(user.id, workspaceId)
        return ApiResponse.success()
    }
}