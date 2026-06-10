package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationNameCountResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.CountResponse;
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

    @Operation(summary = "기능별 사용 내역 조회")
    @GetMapping("/{siteId}/feature")
    public ApiPayload<List<ActivationNameCountResponse>> getActivationNameCounts(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteActivationService.findActivationNameCounts(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(result);
    }

    @Operation(summary = "첫 이벤트 사용 유저 개수 조회")
    @GetMapping("/{siteId}/first-event-users")
    public ApiPayload<CountResponse> getFirstEventUserCount(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteActivationService.findFirstEventUserCount(
            siteId,
            toStartDateTime(start),
            toEndDateTime(end),
            user.id()
        );
        return ApiPayload.success(result);
    }

    @Operation(summary = "기능별 사용자 조회")
    @GetMapping("/{siteId}/event-users")
    public ApiPayload<CountResponse> getEventUserCount(
        @PathVariable UUID siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        var result = siteActivationService.findFirstEventUserCount(
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
