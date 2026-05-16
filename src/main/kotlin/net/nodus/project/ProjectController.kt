package net.nodus.project

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
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Project", description = "프로젝트 관리 API")
@SecurityRequirement(name = "bearerToken")
@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService
) {
    @Operation(summary = "프로젝트 생성", description = "워크스페이스 하위에 새 프로젝트를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateProjectRequest
    ): ApiResponse<ProjectResponse> =
        ApiResponse.success(projectService.create(user.id, request).toResponse())

    @Operation(summary = "프로젝트 목록 조회", description = "특정 워크스페이스에 속한 프로젝트 목록을 조회합니다.")
    @GetMapping
    fun list(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Parameter(description = "프로젝트 목록을 조회할 워크스페이스 ID", required = true)
        @RequestParam workspaceId: String,
    ): ApiResponse<List<ProjectResponse>> =
        ApiResponse.success(projectService.list(user.id, workspaceId).map { it.toResponse() })

    @Operation(summary = "프로젝트 수정", description = "프로젝트 이름 등 수정 가능한 정보를 변경합니다.")
    @PatchMapping("/{projectId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable projectId: String,
        @RequestBody request: UpdateProjectRequest,
    ): ApiResponse<ProjectResponse> =
        ApiResponse.success(projectService.update(user.id, projectId, request).toResponse())

    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
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