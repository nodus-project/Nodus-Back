package net.nodus.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuthCodeRequest {

    @NotBlank
    private String code;

    private String redirectUri;
}
