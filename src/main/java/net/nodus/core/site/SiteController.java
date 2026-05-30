package net.nodus.core.site;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.dto.CreateSiteRequest;
import net.nodus.core.site.dto.IssuedSiteKey;
import net.nodus.core.site.dto.SiteCreateResult;
import net.nodus.core.site.dto.SiteKeyResponse;
import net.nodus.core.site.dto.SiteResponse;
import net.nodus.core.site.dto.UpdateSiteRequest;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.global.config.annotation.CurrentUser;
import net.nodus.security.AuthUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사이트", description = "사이트 관리 API")
@RestController
@RequestMapping("/sites")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @Operation(summary = "사이트 생성", description = "사이트를 생성하고 초기 사이트 키를 발급합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiPayload<SiteResponse> create(
        @CurrentUser AuthUserPrincipal user,
        @Valid @RequestBody CreateSiteRequest request
    ) {
        SiteCreateResult result = siteService.create(user.id(), request);

        return ApiPayload.success(new SiteResponse(
            Objects.requireNonNull(result.site().getId()),
            result.site().getName(),
            result.site().getDomain(),
            result.site().getUrl(),
            result.issuedKey().rawKey()
        ));
    }

    @Operation(summary = "사이트 목록 조회", description = "인증된 사용자의 사이트 목록을 반환합니다.")
    @GetMapping
    public ApiPayload<List<SiteResponse>> list(
        @CurrentUser AuthUserPrincipal user) {
        return ApiPayload.success(
            siteService.list(user.id()).stream().map(SiteResponse::from).toList()
        );
    }

    @Operation(summary = "사이트 조회", description = "사이트 ID로 사이트 정보를 반환합니다.")
    @GetMapping("/{siteId}")
    public ApiPayload<SiteResponse> get(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId
    ) {
        return ApiPayload.success(
            SiteResponse.from(siteService.get(user.id(), siteId)));
    }

    @Operation(summary = "사이트 수정", description = "사이트 메타데이터를 수정합니다.")
    @PatchMapping("/{siteId}")
    public ApiPayload<SiteResponse> update(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId,
        @RequestBody UpdateSiteRequest request
    ) {
        return ApiPayload.success(
            SiteResponse.from(siteService.update(user.id(), siteId, request)));
    }

    @Operation(summary = "사이트 삭제", description = "사이트를 소프트 삭제합니다.")
    @DeleteMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiPayload<Void> delete(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId
    ) {
        siteService.delete(user.id(), siteId);
        return ApiPayload.success();
    }

    @Operation(summary = "사이트 키 조회", description = "활성 사이트 키의 메타데이터를 반환합니다.")
    @GetMapping("/{siteId}/key")
    public ApiPayload<SiteKeyResponse> keyInfo(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId
    ) {
        return ApiPayload.success(
            SiteKeyResponse.from(siteService.keyInfo(user.id(), siteId)));
    }

    @Operation(summary = "사이트 키 교체", description = "활성 키를 폐기하고 새 사이트 키를 발급합니다.")
    @PostMapping("/{siteId}/key/rotate")
    public ApiPayload<SiteKeyResponse> rotateKey(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId
    ) {
        IssuedSiteKey issuedKey = siteService.rotateKey(user.id(), siteId);

        return ApiPayload.success(new SiteKeyResponse(
            Objects.requireNonNull(issuedKey.siteKey().getId()),
            issuedKey.siteKey().getKeyPrefix(),
            issuedKey.rawKey(),
            issuedKey.siteKey().getStatus()
        ));
    }

    @Operation(summary = "사이트 키 폐기", description = "활성 사이트 키를 폐기합니다.")
    @DeleteMapping("/{siteId}/key")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiPayload<Void> revokeKey(
        @CurrentUser AuthUserPrincipal user,
        @PathVariable String siteId
    ) {
        siteService.revokeKey(user.id(), siteId);
        return ApiPayload.success();
    }
}
