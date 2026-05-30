package net.nodus.core.site.controller.dto;

import lombok.Builder;
import net.nodus.database.site.Site;

public class SiteBaseResponse {

    @Builder
    public record SiteOneResponse(
        String id,
        String name,
        String url
    ) {

        public static SiteOneResponse from(Site entity) {
            return SiteOneResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url(entity.getUrl())
                .build();
        }
    }

}
