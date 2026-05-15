package net.nodus.site

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

@RestController
@RequestMapping("/sites")
class SiteController(
    private val siteService: SiteService
) {
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

    @GetMapping
    fun list(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @RequestParam projectId: String,
    ): ApiResponse<List<SiteResponse>> =
        ApiResponse.success(siteService.list(user.id, projectId).map { it.toResponse() })

    @GetMapping("/{siteId}")
    fun get(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<SiteResponse> =
        ApiResponse.success(siteService.get(user.id, siteId).toResponse())

    @PatchMapping("/{siteId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
        @RequestBody request: UpdateSiteRequest,
    ): ApiResponse<SiteResponse> =
        ApiResponse.success(siteService.update(user.id, siteId, request).toResponse())

    @DeleteMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<Void> {
        siteService.delete(user.id, siteId)
        return ApiResponse.success()
    }

    @GetMapping("/{siteId}/key")
    fun keyInfo(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): ApiResponse<SiteKeyResponse> =
        ApiResponse.success(siteService.keyInfo(user.id, siteId).toResponse())

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