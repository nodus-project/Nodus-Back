package net.nodus.core.site.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSiteRequest {

    @NotBlank
    private String name;
    private String domain;
    private String url;

}
