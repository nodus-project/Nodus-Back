package net.nodus.core.site.controller.dto;

import lombok.Builder;
import net.nodus.database.site.Site;

public class SiteKeyResponse {

    @Builder
    public record SiteKeyRecreateResponse(
        String key
    ) {

        public static SiteKeyRecreateResponse from(Site entity) {
            return SiteKeyRecreateResponse.builder()
                .key(entity.getKey())
                .build();
        }
    }
}
