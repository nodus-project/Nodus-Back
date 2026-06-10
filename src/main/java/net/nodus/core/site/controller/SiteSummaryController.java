package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationResponse;
import net.nodus.core.site.service.SiteSummaryService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트 요약 분석", description = "사이트 요약 분석 API")
@RestController
@RequestMapping("/sites/summary")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteSummaryController {

    private final SiteSummaryService siteSummaryService;


    @Operation(summary = "요약")
    @GetMapping("/{siteId}")
    public ApiPayload<ActivationResponse> getActiveUserCount(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteSummaryService.findLogList(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(result);
    }

    private LocalDateTime toStartDateTime(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime toEndDateTime(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }
}
