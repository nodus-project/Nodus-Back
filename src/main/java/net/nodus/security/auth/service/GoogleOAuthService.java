package net.nodus.security.auth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.nodus.security.auth.dto.GoogleOAuthCodeRequest;
import net.nodus.security.auth.service.client.GoogleTokenClient;
import net.nodus.security.auth.service.client.GoogleUserInfoClient;
import net.nodus.global.common.exception.GlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleTokenClient googleTokenClient;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final OAuthLoginService oAuthLoginService;

    @Value("${google.oauth2.client-id}")
    private final String clientId;

    @Value("${google.oauth2.client-secret}")
    private final String clientSecret;

    public OAuthLoginService.OAuthLoginResult login(GoogleOAuthCodeRequest dto) {
        String googleAccessToken = exchangeCode(dto);
        GoogleUserInfo userInfo = fetchUserInfo(googleAccessToken);

        return oAuthLoginService.loginGoogleUser(
            userInfo.subject(),
            userInfo.email(),
            userInfo.name() == null ? userInfo.email() : userInfo.name()
        );
    }

    private String exchangeCode(GoogleOAuthCodeRequest dto) {
        Map<String, String> params = Map.of(
            "code", dto.getCode(),
            "client_id", clientId,
            "client_secret", clientSecret,
            "redirect_uri", dto.getRedirectUri(),
            "grant_type", "authorization_code"
        );

        Map<String, Object> response = googleTokenClient.exchangeCode(params);
        Object accessToken = response.get("access_token");
        if (accessToken instanceof String token) {
            return token;
        }
        throw new GlobalException.ExternalApiFailed("Google access token is missing");
    }

    private GoogleUserInfo fetchUserInfo(String accessToken) {
        Map<String, Object> response = googleUserInfoClient.fetchUserInfo("Bearer " + accessToken);

        return new GoogleUserInfo(
            (String) response.get("sub"),
            (String) response.get("email"),
            (String) response.get("name")
        );
    }

    public record GoogleUserInfo(String subject, String email, String name) {

    }
}
