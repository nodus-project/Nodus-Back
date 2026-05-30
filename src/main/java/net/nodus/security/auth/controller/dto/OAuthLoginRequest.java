package net.nodus.security.auth.controller.dto;

public class OAuthLoginRequest {

    public record GoogleOAuthLoginRequest(
        String code,
        String redirectUri
    ) {

    }
}
