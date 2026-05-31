package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteBaseRequest;
import net.nodus.core.site.controller.dto.SiteBaseRequest.UpdateSiteRequest;
import net.nodus.core.site.controller.dto.SiteBaseResponse.SiteOneResponse;
import net.nodus.core.site.service.SiteBaseService;
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
public class SiteBaseController {

    private final SiteBaseService siteBaseService;

    @Operation(summary = "사이트 생성")
    @PostMapping
    public ApiPayload<SiteOneResponse> create(
        @Valid @RequestBody SiteBaseRequest.SiteCreateRequest dto,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteBaseService.create(dto, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 목록 조회")
    @GetMapping
    public ApiPayload<List<SiteOneResponse>> findAll(
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteBaseService.getSiteList(user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 조회")
    @GetMapping("/{siteId}")
    public ApiPayload<SiteOneResponse> findOne(
        @PathVariable String siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteBaseService.getSite(siteId, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 수정")
    @PatchMapping("/{siteId}")
    public ApiPayload<SiteOneResponse> update(
        @PathVariable String siteId,
        @RequestBody UpdateSiteRequest dto,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteBaseService.updateSite(siteId, dto, user.id());
        return ApiPayload.success(result);
    }

    @Operation(summary = "사이트 삭제")
    @DeleteMapping("/{siteId}")
    public ApiPayload<Void> delete(
        @PathVariable String siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        siteBaseService.deleteSite(siteId, user.id());
        return ApiPayload.success();
    }
}
