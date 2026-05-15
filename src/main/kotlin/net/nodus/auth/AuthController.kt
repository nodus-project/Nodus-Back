package net.nodus.auth

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import net.nodus.auth.service.GoogleOAuthService
import net.nodus.auth.service.JwtTokenService
import net.nodus.auth.service.RefreshTokenService
import net.nodus.config.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/auth")
class AuthController(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenService: JwtTokenService,
    private val googleOAuthService: GoogleOAuthService,
) {
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<TokenRefreshResponse> {
        val issuedRefreshToken = refreshTokenService.rotate(request.refreshToken)
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

        val refreshTokenExpirationSeconds: Long = 60 * 60 * 24 * 7 * 4 // ec2 에러 때매 옮김
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
