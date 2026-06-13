package net.nodus.core.site.controller.dto;

import lombok.Builder;

public class SiteActivationResponse {

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
}
