package net.nodus.core.site.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class SiteBaseRequest {

    public record SiteCreateRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotBlank(message = "주소를 입력해주세요.")
        String url
    ) {

    }

    public record UpdateSiteRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotBlank(message = "주소를 입력해주세요.")
        String url
    ) {

    }
}
