package net.nodus.security.auth.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.nodus.database.account.OAuthProvider;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.security.auth.controller.dto.OAuthLoginRequest.GoogleOAuthLoginRequest;
import net.nodus.security.auth.controller.dto.UserAccountDetails;
import net.nodus.security.auth.service.client.GoogleTokenClient;
import net.nodus.security.auth.service.client.GoogleUserInfoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleTokenClient googleTokenClient;
    private final GoogleUserInfoClient googleUserInfoClient;

    private final UserAccountRepository userAccountRepository;

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    public UserAccountDetails login(GoogleOAuthLoginRequest dto) {

        Map<String, String> params = Map.of(
            "code", dto.code(),
            "client_id", clientId,
            "client_secret", clientSecret,
            "redirect_uri", dto.redirectUri(),
            "grant_type", "authorization_code"
        );

        GoogleTokenInfo tokenInfo = googleTokenClient.exchangeCode(params);
        GoogleUserInfo userInfo = googleUserInfoClient.fetchUserInfo(
            "Bearer " + tokenInfo.accessToken);

        UserAccount user = userAccountRepository
            .findByProviderAndProviderId(
                OAuthProvider.GOOGLE,
                userInfo.sub()
            )
            .orElseGet(() -> {
                UserAccount userAccount = UserAccount.builder()
                    .providerId(userInfo.sub())
                    .name(userInfo.name())
                    .email(userInfo.email())
                    .provider(OAuthProvider.GOOGLE)
                    .build();
                return userAccountRepository.save(userAccount);
            });

        return new UserAccountDetails(user);
    }

    public record GoogleTokenInfo(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("scope")
        String scope,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("id_token")
        String idToken
    ) {

    }

    public record GoogleUserInfo(
        @JsonProperty("sub")
        String sub,

        @JsonProperty("name")
        String name,

        @JsonProperty("given_name")
        String givenName,

        @JsonProperty("family_name")
        String familyName,

        @JsonProperty("picture")
        String picture,

        @JsonProperty("email")
        String email,

        @JsonProperty("email_verified")
        Boolean emailVerified,

        @JsonProperty("locale")
        String locale
    ) {

    }
}
