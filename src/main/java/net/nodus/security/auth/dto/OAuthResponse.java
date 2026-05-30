package net.nodus.security.auth.dto;

import lombok.Builder;

public class OAuthResponse {

    @Builder
    public static class TokenRefreshResponse {

        private String accessToken;

        @Builder.Default
        private String tokenType = "Bearer";
    }

    @Builder
    public static class OAuthLoginResponse {

        private String accessToken;
        private String refreshToken;

        @Builder.Default
        private String tokenType = "Bearer";
    }

}
