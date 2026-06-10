package net.nodus.core.site.controller.dto;

import lombok.Builder;
import net.nodus.database.sdk.SiteActivationLogRepository.ActivationFeatureCount;

public class SiteActivationResponse {

    @Builder
    public record ActivationResponse(
        Long activationLogCount,
        Long visitLogCount,
        Long revenueLogCount
    ) {

        public static ActivationResponse from(
            Long activationCount,
            Long visitCount,
            Long revenueCount
        ) {
            return ActivationResponse.builder()
                .activationLogCount(activationCount)
                .visitLogCount(visitCount)
                .revenueLogCount(revenueCount)
                .build();
        }
    }

    @Builder
    public record CountResponse(
        Long count
    ) {

        public static CountResponse from(Long count) {
            return CountResponse.builder()
                .count(count)
                .build();
        }
    }

    @Builder
    public record ActivationNameCountResponse(
        String name,
        Long count
    ) {

        public static ActivationNameCountResponse from(ActivationFeatureCount activationNameCount) {
            return ActivationNameCountResponse.builder()
                .name(activationNameCount.getName())
                .count(activationNameCount.getCount())
                .build();
        }
    }
}
