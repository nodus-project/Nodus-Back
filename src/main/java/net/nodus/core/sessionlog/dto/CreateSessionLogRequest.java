package net.nodus.core.sessionlog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSessionLogRequest {

    @NotBlank
    private String siteKey;

    @NotBlank
    private String userSession;
}
