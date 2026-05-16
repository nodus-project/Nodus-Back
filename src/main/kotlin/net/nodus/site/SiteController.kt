package net.nodus.site

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import jakarta.validation.Valid
import net.nodus.config.ApiResponse
import net.nodus.security.AuthUserPrincipal
import net.nodus.site.dto.CreateSiteRequest
import net.nodus.site.dto.SiteKeyResponse
import net.nodus.site.dto.SiteResponse
import net.nodus.site.dto.UpdateSiteRequest
import net.nodus.site.dto.toResponse
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

@Tag(name = "Site", description = "사이트 관리 API")
@RestController
@RequestMapping("/sites")
@SecurityRequirement(name = "bearerToken")
class SiteController(
    private val siteService: SiteService
) {
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "201", description = "사이트 생성 성공"),
            SwaggerApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            SwaggerApiResponse(responseCode = "401", description = "인증 실패"),
        ]
    )

    @Operation(summary = "사이트 생성", description = "프로젝트에 사이트를 등록하고 최초 사이트 키를 발급합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateSiteRequest,
    ): ApiResponse<SiteResponse> {
        val result = siteService.create(user.id, request)

        return ApiResponse.success(
            SiteResponse(
                id = requireNotNull(result.site.id),
                name = result.site.name,
                domain = result.site.domain,
                url = result.site.url,
                siteKey = result.issuedKey.rawKey,
            ),
        )
    }

    @Operation(summary = "사이트 목록 조회", description = "특정 워크스페이스에 등록된 사이트 목록을 조회합니다.")
    @GetMapping
    fun list(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @RequestParam workspaceId: String,
    ): ApiResponse<List<SiteResponse>> =
        ApiResponse.success(siteService.list(user.id, workspaceId).map { it.toResponse() })

    @Operation(summary = "사이트 단건 조회", description = "사이트 ID로 사이트 상세 정보를 조회합니다.")
    @GetMapping("/{siteId}")
    fun get(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<SiteResponse> =
        ApiResponse.success(siteService.get(user.id, siteId).toResponse())

    @Operation(summary = "사이트 수정", description = "사이트 이름, 도메인, URL 정보를 수정합니다.")
    @PatchMapping("/{siteId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
        @RequestBody request: UpdateSiteRequest,
    ): ApiResponse<SiteResponse> =
        ApiResponse.success(siteService.update(user.id, siteId, request).toResponse())

    @Operation(summary = "사이트 삭제", description = "사이트를 삭제합니다.")
    @DeleteMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<Void> {
        siteService.delete(user.id, siteId)
        return ApiResponse.success()
    }

    @Operation(summary = "사이트 키 조회", description = "사이트의 활성 키 정보를 조회합니다. 원본 키 값은 다시 노출되지 않습니다.")
    @GetMapping("/{siteId}/key")
    fun keyInfo(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<SiteKeyResponse> =
        ApiResponse.success(siteService.keyInfo(user.id, siteId).toResponse())

    @Operation(summary = "사이트 키 회전", description = "기존 활성 사이트 키를 폐기하고 새 사이트 키를 발급합니다.")
    @PostMapping("/{siteId}/key/rotate")
    fun rotateKey(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<SiteKeyResponse> {
        val issuedKey = siteService.rotateKey(user.id, siteId)

        return ApiResponse.success(
            SiteKeyResponse(
                keyId = requireNotNull(issuedKey.siteKey.id),
                keyPrefix = issuedKey.siteKey.keyPrefix,
                siteKey = issuedKey.rawKey,
                status = issuedKey.siteKey.status,
            ),
        )
    }

    @Operation(summary = "사이트 키 폐기", description = "사이트의 활성 키를 폐기합니다.")
    @DeleteMapping("/{siteId}/key")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun revokeKey(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<Void> {
        siteService.revokeKey(user.id, siteId)
        return ApiResponse.success()
    }
}