package net.nodus.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.security.auth.controller.dto.OAuthLoginRequest;
import net.nodus.security.auth.controller.dto.UserAccountDetails;
import net.nodus.security.auth.service.GoogleOAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 및 토큰 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final AuthManager authManager;

    @Operation(
        summary = "Google OAuth 코드 로그인",
        description = "Google OAuth 인증 코드로 로그인하고 토큰을 발급합니다."
    )
    @PostMapping("/oauth2/google/code")
    public ApiPayload<Void> googleCodeLogin(
        @Valid @RequestBody OAuthLoginRequest.GoogleOAuthLoginRequest dto,
        HttpServletResponse response
    ) {
        UserAccountDetails userAccountDetails = googleOAuthService.login(dto);
        authManager.updateAuthData(userAccountDetails, response);
        return ApiPayload.success();
    }
}
