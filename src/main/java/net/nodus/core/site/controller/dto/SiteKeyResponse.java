package net.nodus.core.site.controller.dto;

import lombok.Builder;
import net.nodus.database.site.Site;

public class SiteKeyResponse {

    @Builder
    public record SiteKeyOneResponse(
        String key
    ) {

        public static SiteKeyOneResponse from(Site entity) {
            return SiteKeyOneResponse.builder()
                .key(entity.getKey())
                .build();
        }
    }
}
