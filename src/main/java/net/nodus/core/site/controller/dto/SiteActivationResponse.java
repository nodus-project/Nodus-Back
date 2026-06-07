package net.nodus.core.site.controller.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import net.nodus.database.sdk.SiteActivationLogRepository.ActivationNameCount;
import net.nodus.database.sdk.SiteActivationLogRepository.FirstEventUser;

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

        public static ActivationNameCountResponse from(ActivationNameCount activationNameCount) {
            return ActivationNameCountResponse.builder()
                .name(activationNameCount.getName())
                .count(activationNameCount.getCount())
                .build();
        }
    }

    @Builder
    public record FirstEventUserResponse(
        String sessionId,
        LocalDateTime firstEventAt
    ) {

        public static FirstEventUserResponse from(FirstEventUser firstEventUser) {
            return FirstEventUserResponse.builder()
                .sessionId(firstEventUser.getSessionId())
                .firstEventAt(firstEventUser.getFirstEventAt())
                .build();
        }
    }
}
