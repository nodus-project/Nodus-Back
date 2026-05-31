package net.nodus.security.auth.controller.dto;

import lombok.Builder;

@Builder
public record OAuthLoginResponse(
    String accessToken,
    String refreshToken
) {

}