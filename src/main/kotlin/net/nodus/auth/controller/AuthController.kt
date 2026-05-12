package net.nodus.auth

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import net.nodus.auth.service.GoogleOAuthService
import net.nodus.auth.service.JwtTokenService
import net.nodus.auth.service.RefreshTokenService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Duration

@RestController
@RequestMapping("/auth")
class AuthController(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenService: JwtTokenService,
    private val googleOAuthService: GoogleOAuthService,

    private val refreshTokenExpirationSeconds: Long,
) {
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<TokenRefreshResponse> {
        val issuedRefreshToken = try {
            refreshTokenService.rotate(request.refreshToken)
        } catch (_: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val accessToken = jwtTokenService.createAccessToken(issuedRefreshToken.userAccount)

        return ResponseEntity.ok(
            TokenRefreshResponse(
                accessToken = accessToken,
                refreshToken = issuedRefreshToken.token,
            )
        )
    }

    @PostMapping("/oauth2/google/code")
    fun googleCodeLogin(
        @Valid @RequestBody request: GoogleOAuthCodeRequest,
        response: HttpServletResponse,
    ): ApiResponse<Void> {
        val loginResult = googleOAuthService.login(request)

        val refreshCookie = ResponseCookie.from("refreshToken", loginResult.refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofSeconds(refreshTokenExpirationSeconds))
            .build()

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ${loginResult.accessToken}")
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        response.addHeader("X-Client-Key", loginResult.clientKey)

        return ApiResponse.success()
    }

}

data class RefreshTokenRequest(
    @field:NotBlank
    val refreshToken: String,
)

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
)

data class GoogleOAuthCodeRequest(
    @field:NotBlank
    val code: String,
    val redirectUri: String,
)
