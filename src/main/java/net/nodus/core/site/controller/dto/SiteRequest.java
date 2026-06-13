package net.nodus.core.site.controller.dto;

public class SiteRequest {

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
