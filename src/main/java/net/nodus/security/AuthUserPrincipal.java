package net.nodus.security;

import java.util.UUID;
import lombok.Builder;
import net.nodus.security.auth.controller.dto.UserAccountDetails;

@Builder
public record AuthUserPrincipal(
    UUID id
) {

    public static AuthUserPrincipal from(UserAccountDetails userAccountDetails) {
        return AuthUserPrincipal.builder()
            .id(userAccountDetails.getUser().getId())
            .build();
    }
}
