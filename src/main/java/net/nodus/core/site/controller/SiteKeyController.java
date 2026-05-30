package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteKeyResponse.SiteKeyRecreateResponse;
import net.nodus.core.site.service.SiteKeyService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트", description = "사이트 관리 API")
@RestController
@RequestMapping("/sites/keys")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteKeyController {

    private final SiteKeyService siteKeyService;

    @Operation(summary = "사이트 키 재발급")
    @GetMapping("/{siteId}")
    public ApiPayload<SiteKeyRecreateResponse> findOne(
        @PathVariable String siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteKeyService.recreateKey(siteId, user.id());
        return ApiPayload.success(result);
    }

}
