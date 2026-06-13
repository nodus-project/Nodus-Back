package net.nodus.core.site.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import net.nodus.database.sdk.SiteTrafficSource;
import org.springframework.util.StringUtils;

public class SiteTrafficSourceResponse {

    @Builder
    public record TrafficSourceSummaryResponse(
        String channel,
        Long count
    ) {

        public static List<TrafficSourceSummaryResponse> from(
            List<SiteTrafficSource> trafficSourceList
        ) {
            return trafficSourceList.stream()
                .collect(Collectors.groupingBy(
                    trafficSource -> StringUtils.hasText(trafficSource.getChannel().name())
                        ? trafficSource.getChannel().name().toLowerCase()
                        : ""
                ))
                .entrySet()
                .stream()
                .map(entry -> TrafficSourceSummaryResponse.builder()
                    .channel(entry.getKey())
                    .count((long) entry.getValue().size())
                    .build())
                .toList();
        }
    }
}
