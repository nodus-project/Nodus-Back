package net.nodus.core.sdk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nodus.core.sdk.controller.dto.SdkLogRequest;
import net.nodus.core.sdk.service.SdkLogService;
import net.nodus.global.common.response.ApiPayload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SDK 방문", description = "SDK 방문 로그 API")
@RestController
@RequestMapping("/sdk")
@RequiredArgsConstructor
public class SdkController {

    private final SdkLogService sdkLogService;

    @Operation(summary = "트래픽 소스 로그")
    @PostMapping("/traffic-source")
    public ApiPayload<Void> trafficSource(
        @RequestParam String key,
        @Valid @RequestBody SdkLogRequest.SdkTrafficSourcePostRequest dto
    ) {
        sdkLogService.trafficSourceLog(key, dto);
        return ApiPayload.success();
    }

    @Operation(summary = "사이트 접속 로그")
    @PostMapping("/visit")
    public ApiPayload<Void> visit(
        @RequestParam String key,
        @Valid @RequestBody SdkLogRequest.SdkVisitPostRequest dto
    ) {
        sdkLogService.visitLog(key, dto);
        return ApiPayload.success();
    }

    @Operation(summary = "활동 로그")
    @PostMapping("/activation")
    public ApiPayload<Void> activation(
        @RequestParam String key,
        @Valid @RequestBody SdkLogRequest.SdkActivationPostRequest dto
    ) {
        sdkLogService.activationLog(key, dto);
        return ApiPayload.success();
    }

    @Operation(summary = "결제 로그")
    @PostMapping("/revenue")
    public ApiPayload<Void> revenue(
        @RequestParam String key,
        @Valid @RequestBody SdkLogRequest.SdkRevenuePostRequest dto
    ) {
        sdkLogService.revenueLog(key, dto);
        return ApiPayload.success();
    }

}
