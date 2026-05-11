package net.nodus.auth.service

import net.nodus.auth.controller.GoogleOAuthCodeRequest
import net.nodus.auth.service.client.GoogleTokenClient
import net.nodus.auth.service.client.GoogleUserInfoClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoogleOAuthService(
    private val googleTokenClient: GoogleTokenClient,
    private val googleUserInfoClient: GoogleUserInfoClient,
    private val oAuthLoginService: OAuthLoginService,

    @Value("\${google.oauth2.client-id}")
    private val clientId: String,

    @Value("\${google.oauth2.client-secret}")
    private val clientSecret: String
) {

    fun login(dto: GoogleOAuthCodeRequest): OAuthLoginResult {
        val googleAccessToken = exchangeCode(dto)
        val userInfo = fetchUserInfo(googleAccessToken)

        return oAuthLoginService.loginGoogleUser(
            providerId = userInfo.subject,
            email = userInfo.email,
            name = userInfo.name ?: userInfo.email,
        )
    }

    private fun exchangeCode(dto: GoogleOAuthCodeRequest): String {
        val params = mapOf(
            "code" to dto.code,
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "redirect_uri" to dto.redirectUri,
            "grant_type" to "authorization_code"
        )

        val response = googleTokenClient.exchangeCode(params)

        return response["access_token"] as? String
            ?: throw IllegalStateException("Google access token is missing")
    }

    private fun fetchUserInfo(accessToken: String): GoogleUserInfo {
        val response = googleUserInfoClient.fetchUserInfo("Bearer $accessToken")

        return GoogleUserInfo(
            subject = response["sub"] as String,
            email = response["email"] as String,
            name = response["name"] as? String,
        )
    }
}

data class GoogleUserInfo(
    var subject: String,
    var email: String,
    var name: String?,
)