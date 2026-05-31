package net.nodus.core.site.controller.dto;

import java.util.List;
import lombok.Builder;
import net.nodus.database.sdk.SiteVisitLog;

public class SiteVisitResponse {

    @Builder
    public record SiteVisitCountResponse(
        Long count
    ) {

        public static SiteVisitCountResponse from(List<SiteVisitLog> entities) {
            long count = entities.stream()
                .map(SiteVisitLog::getSessionId)
                .filter(sessionId -> sessionId != null && !sessionId.isBlank())
                .distinct()
                .count();

            return SiteVisitCountResponse.builder()
                .count(count)
                .build();
        }
    }
}
