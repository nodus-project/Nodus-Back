package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteSummaryResponse.ActivationResponse;
import net.nodus.core.site.controller.dto.SiteSummaryResponse.TrafficSourceSummaryResponse;
import net.nodus.core.site.service.SiteSummaryService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트 요약 분석", description = "사이트 요약 분석 API")
@RestController
@RequestMapping("/sites/summary")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteSummaryController {

    private final SiteSummaryService siteSummaryService;


    @Operation(summary = "로그 요약")
    @GetMapping("/{siteId}")
    public ApiPayload<ActivationResponse> getActiveUserCount(
        @PathVariable UUID siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteSummaryService.findLogList(
            siteId,
            user.id()
        );
        return ApiPayload.success(result);
    }

    @Operation(summary = "유입 경로 목록")
    @GetMapping("/{siteId}/traffic-sources")
    public ApiPayload<List<TrafficSourceSummaryResponse>> getTrafficSources(
        @PathVariable UUID siteId,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteSummaryService.findTrafficSourceSummary(
            siteId,
            user.id()
        );
        return ApiPayload.success(result);
    }

}
