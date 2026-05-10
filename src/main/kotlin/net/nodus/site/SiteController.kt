package net.nodus.site

import jakarta.validation.Valid
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
@RequestMapping("/sites")
class SiteController(
    private val siteService: SiteService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @Valid @RequestBody request: CreateSiteRequest,
    ): SiteResponse {
        val result = siteService.create(user.id, request)

        return SiteResponse(
            id = requireNotNull(result.site.id),
            name = result.site.name,
            domain = result.site.domain,
            url = result.site.url,
            clientKey = result.issuedKey.rawKey,
        )
    }

    @GetMapping
    fun list(
        @AuthenticationPrincipal user: AuthUserPrincipal,
    ): List<SiteResponse> =
        siteService.list(user.id).map { it.toResponse()  }

    @GetMapping("/siteId")
    fun get(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): SiteResponse =
        siteService.get(user.id, siteId).toResponse()

    @PatchMapping("/{siteId}")
    fun update(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
        @RequestBody request: UpdateSiteRequest,
    ): SiteResponse =
        siteService.update(user.id, siteId, request).toResponse()

    @DeleteMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ) {
        siteService.delete(user.id, siteId)
    }

    @GetMapping("/{siteId}/key")
    fun keyInfo(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): SiteKeyResponse =
        siteService.keyInfo(user.id, siteId).toResponse()

    @PostMapping("/{siteId}/key/rotate")
    fun rotateKey(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ): SiteKeyResponse {
        val issuedKey = siteService.rotateKey(user.id, siteId)

        return SiteKeyResponse(
            keyId = requireNotNull(issuedKey.siteKey.id),
            keyPrefix = issuedKey.siteKey.keyPrefix,
            clientKey = issuedKey.rawKey,
            status = issuedKey.siteKey.status,
        )
    }

    @DeleteMapping("/{siteId}/key")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun revokeKey(
        @AuthenticationPrincipal user: AuthUserPrincipal,
        @PathVariable siteId: String,
    ) {
        siteService.revokeKey(user.id, siteId)
    }

    private fun Site.toResponse(): SiteResponse =
        SiteResponse(
            id = requireNotNull(id),
            name = name,
            domain = domain,
            url = url,
        )

    private fun SiteKey.toResponse(): SiteKeyResponse =
        SiteKeyResponse(
            keyId = requireNotNull(id),
            keyPrefix = keyPrefix,
            status = status,
        )
}