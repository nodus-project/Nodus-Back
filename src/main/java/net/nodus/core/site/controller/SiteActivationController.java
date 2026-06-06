package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationRateResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActiveUserCountResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.FirstEventCountResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.TimeToActivationResponse;
import net.nodus.core.site.service.SiteActivationService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트 활성화", description = "사이트 활성화 분석 API")
@RestController
@RequestMapping("/sites/activation")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteActivationController {

    private final SiteActivationService siteActivationService;

    @Operation(summary = "활성화 사용자 조회")
    @GetMapping("/{siteId}/active-users")
    public ApiPayload<ActiveUserCountResponse> getActiveUserCount(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteActivationService.findActiveUserLogs(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(result);
    }

    @Operation(summary = "유입 대비 활성화 조회")
    @GetMapping("/{siteId}/activation-rate")
    public ApiPayload<ActivationRateResponse> getActivationRate(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteActivationService.findActivationRateLogs(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(result);
    }

    @Operation(summary = "첫 가치 경험 시간 조회")
    @GetMapping("/{siteId}/time-to-activation")
    public ApiPayload<TimeToActivationResponse> getTimeToActivation(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var logs = siteActivationService.findTimeToActivationLogs(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(TimeToActivationResponse.from(logs));
    }

    @Operation(summary = "첫 이벤트 수집 조회")
    @GetMapping("/{siteId}/first-event")
    public ApiPayload<FirstEventCountResponse> getFirstEventCount(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var logs = siteActivationService.findFirstEventLogs(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(FirstEventCountResponse.from(logs));
    }

    private LocalDateTime toStartDateTime(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime toEndDateTime(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }
}
