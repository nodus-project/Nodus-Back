package net.nodus.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import java.lang.IllegalStateException

@Service
class GoogleOAuthService(
    restClientBuilder: RestClient.Builder,
    private val oAuthLoginService: OAuthLoginService,

    @Value("\${google.oauth2.client-id}")
    private val clientId: String,

    @Value("\${google.oauth2.client-secret}")
    private val clientSecret: String,

    @Value("\${google.oauth2.redirect-uri}")
    private val redirectUri: String,
) {
     private val restClient = restClientBuilder.build()

    fun login(code: String): OAuthLoginResult {
        val googleAccessToken = exchangeCode(code)
        val userInfo = fetchUserInfo(googleAccessToken)

        return oAuthLoginService.loginGoogleUser(
            providerId = userInfo.subject,
            email = userInfo.email,
            name = userInfo.name ?: userInfo.email,
        )
    }

    private fun exchangeCode(code: String): String {
        val form = LinkedMultiValueMap<String, String>()
        form.add("code", code)
        form.add("client_id", clientId)
        form.add("client_secret", clientSecret)
        form.add("redirect_uri", redirectUri)
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