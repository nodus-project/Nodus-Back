package net.nodus.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import net.nodus.auth.service.GoogleOAuthService
import net.nodus.auth.service.JwtTokenService
import net.nodus.auth.service.RefreshTokenService
import net.nodus.common.exception.GlobalException
import net.nodus.common.response.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Tag(name = "Auth", description = "인증 및 토큰 관리 API")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenService: JwtTokenService,
    private val googleOAuthService: GoogleOAuthService,
) {
    @Operation(
        summary = "엑세스 토큰 재발급",
        description = "리프레시 토큰을 검증하고 회전시킨 뒤 엑세스 토큰과 리프레시 토큰을 발급합니다."
    )
    @PostMapping("/refresh")
    fun refresh(
        @CookieValue("refreshToken", required = false)
        refreshToken: String?,
        response: HttpServletResponse): ResponseEntity<TokenRefreshResponse> {

        if(refreshToken.isNullOrBlank()) {
            throw GlobalException.Unauthorized("Missing refresh token.")
        }

        val issuedRefreshToken = refreshTokenService.rotate(refreshToken)
        val accessToken = jwtTokenService.createAccessToken(issuedRefreshToken.userAccount)

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(issuedRefreshToken.token).toString()
        )

        return ResponseEntity.ok(
            TokenRefreshResponse(
                accessToken = accessToken
            )
        )

    }

    @Operation(
        summary = "Google OAuth 코드 로그인",
        description = "프론트엔드에서 전달한 Google OAuth authorization code로 로그인하고, 액세스 토큰과 리프레시 쿠키를 발급합니다."
    )
    @PostMapping("/oauth2/google/code")
    fun googleCodeLogin(
        @Valid @RequestBody request: GoogleOAuthCodeRequest,
        response: HttpServletResponse,
    ): ApiResponse<Void> {
        val loginResult = googleOAuthService.login(request)

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ${loginResult.accessToken}")
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshCookie(loginResult.refreshToken).toString()
        )

        return ApiResponse.success()
    }


    private fun createRefreshCookie(refreshToken: String): ResponseCookie {
        val refreshTokenExpirationSeconds: Long = 60 * 60 * 24 * 7 * 4

        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofSeconds(refreshTokenExpirationSeconds))
            .build()
    }
}

data class TokenRefreshResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
)

data class GoogleOAuthCodeRequest(
    @field:NotBlank
    val code: String,
    val redirectUri: String,
)
