package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteRequest;
import net.nodus.core.site.controller.dto.SiteRequest.UpdateSiteRequest;
import net.nodus.core.site.controller.dto.SiteResponse.SiteOneResponse;
import net.nodus.core.site.service.SiteService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트", description = "사이트 관리 API")
@RestController
@RequestMapping("/sites")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @Operation(summary = "사이트 생성")
    @PostMapping
    public ApiPayload<SiteOneResponse> create(
        @Valid @RequestBody SiteRequest.SiteCreateRequest dto,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteService.create(dto, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 목록 조회")
    @GetMapping
    public ApiPayload<List<SiteOneResponse>> findAll(
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteService.getSiteList(user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 조회")
    @GetMapping("/{siteId}")
    public ApiPayload<SiteOneResponse> findOne(
        @PathVariable UUID siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteService.getSite(siteId, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 수정")
    @PatchMapping("/{siteId}")
    public ApiPayload<SiteOneResponse> update(
        @PathVariable UUID siteId,
        @RequestBody UpdateSiteRequest dto,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteService.updateSite(siteId, dto, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 삭제")
    @DeleteMapping("/{siteId}")
    public ApiPayload<Void> delete(
        @PathVariable UUID siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        siteService.deleteSite(siteId, user.id());
        return ApiPayload.success();
    }
}
