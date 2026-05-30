package net.nodus.security;

import lombok.Builder;
import net.nodus.security.auth.controller.dto.UserAccountDetails;

@Builder
public record AuthUserPrincipal(
    String id
) {

    public static AuthUserPrincipal from(UserAccountDetails userAccountDetails) {
        return AuthUserPrincipal.builder()
            .id(userAccountDetails.getUsername())
            .build();
    }
}
