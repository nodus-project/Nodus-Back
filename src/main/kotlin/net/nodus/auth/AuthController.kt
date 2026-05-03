package net.nodus.auth

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenService: JwtTokenService,
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
