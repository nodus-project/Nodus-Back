package net.nodus.core.sdk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nodus.core.sdk.controller.dto.SdkVisitRequest.SdkVisitPostRequest;
import net.nodus.core.sdk.service.SdkVisitService;
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
public class SdkVisitController {

    private final SdkVisitService sdkVisitService;

    @Operation(summary = "사이트 접속 로그")
    @PostMapping("/visit")
    public ApiPayload<Void> create(
        @RequestParam String key,
        @Valid @RequestBody SdkVisitPostRequest dto
    ) {
        sdkVisitService.create(key, dto);
        return ApiPayload.success();
    }
}
