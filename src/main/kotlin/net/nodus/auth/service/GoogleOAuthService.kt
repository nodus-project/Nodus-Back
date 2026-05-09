package net.nodus.auth.service

import net.nodus.auth.controller.GoogleOAuthCodeRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import java.lang.IllegalStateException
import kotlin.collections.get

@Service
class GoogleOAuthService(
    private var restClient: RestClient,

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
        val form = LinkedMultiValueMap<String, String>()
        form.add("code", dto.code)
        form.add("client_id", clientId)
        form.add("client_secret", clientSecret)
        form.add("redirect_uri", dto.redirectUri)
        form.add("grant_type", "authorization_code")

        val response = restClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(Map::class.java)
            ?: throw IllegalStateException("Google token response is empty")

        return response["access_token"] as? String
            ?: throw IllegalStateException("Google access token is missing")
    }

    private fun fetchUserInfo(accessToken: String): GoogleUserInfo {
        val response = restClient.get()
            .uri("https://openidconnect.googleapis.com/v1/userinfo")
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .body(Map::class.java)
            ?: throw IllegalStateException("Google user info response is empty")

        return GoogleUserInfo(
            subject = response["sub"] as String,
            email = response["email"] as String,
            name = response["name"] as? String,
        )
    }
}

data class GoogleUserInfo(
    val subject: String,
    val email: String,
    val name: String?,
)