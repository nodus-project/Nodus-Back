package net.nodus.core.site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteVisitResponse.SiteVisitCountResponse;
import net.nodus.core.site.service.SiteVisitService;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.RoleUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트", description = "사이트 관리 API")
@RestController
@RequestMapping("/sites/visit")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteVisitController {

    private final SiteVisitService siteVisitService;

    @Operation(summary = "사이트 방문 사용자 수 조회")
    @GetMapping("/{siteId}")
    public ApiPayload<SiteVisitCountResponse> getAllUser(
        @PathVariable String siteId,
        @RequestParam("start") LocalDate start,
        @RequestParam("end") LocalDate end,
        @RoleUser AuthUserPrincipal user
    ) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

        var result = siteVisitService.findVisitCount(
            siteId,
            startDateTime,
            endDateTime,
            user.id()
        );
        return ApiPayload.success(result);
    }
}
