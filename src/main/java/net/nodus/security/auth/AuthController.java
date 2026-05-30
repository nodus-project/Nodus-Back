package net.nodus.security.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import net.nodus.global.common.response.ApiPayload;
import net.nodus.security.auth.dto.GoogleOAuthCodeRequest;
import net.nodus.security.auth.service.GoogleOAuthService;
import net.nodus.security.auth.service.OAuthLoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    @Operation(
        summary = "Google OAuth 코드 로그인",
        description = "Google OAuth 인증 코드로 로그인하고 토큰을 발급합니다."
    )
    @PostMapping("/oauth2/google/code")
    public ApiPayload<Void> googleCodeLogin(
        @Valid @RequestBody GoogleOAuthCodeRequest request,
        HttpServletResponse response
    ) {
        OAuthLoginService.OAuthLoginResult loginResult = googleOAuthService.login(request);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + loginResult.accessToken());
        response.addHeader(HttpHeaders.SET_COOKIE,
            createRefreshCookie(loginResult.refreshToken()).toString());

        return ApiPayload.success();
    }

    private ResponseCookie createRefreshCookie(String refreshToken) {
        long refreshTokenExpirationSeconds = 60L * 60 * 24 * 7 * 4;

        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofSeconds(refreshTokenExpirationSeconds))
            .build();
    }
}
