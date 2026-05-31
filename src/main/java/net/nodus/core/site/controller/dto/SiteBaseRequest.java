package net.nodus.core.site.controller.dto;

public class SiteBaseRequest {

    public record SiteCreateRequest(
        String name,
        String url
    ) {

    }

    public record UpdateSiteRequest(
        String name,
        String url
    ) {

    }
}
