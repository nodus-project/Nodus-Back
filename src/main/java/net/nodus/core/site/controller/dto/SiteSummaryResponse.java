package net.nodus.core.site.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import net.nodus.database.sdk.SiteTrafficSource;
import org.springframework.util.StringUtils;

public class SiteSummaryResponse {

    @Builder
    public record ActivationResponse(
        Long acquisitionCount,
        Long activationCount,
        Long revenueCount,
        Long retentionCount
    ) {

        public static ActivationResponse from(
            Long acquisitionCount,
            Long activationCount,
            Long revenueCount,
            Long retentionCount
        ) {
            return ActivationResponse.builder()
                .acquisitionCount(acquisitionCount)
                .activationCount(activationCount)
                .revenueCount(revenueCount)
                .retentionCount(retentionCount)
                .build();
        }
    }

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
