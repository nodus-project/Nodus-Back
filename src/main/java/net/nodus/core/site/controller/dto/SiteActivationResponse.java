package net.nodus.core.site.controller.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import net.nodus.core.site.service.SiteActivationService.FirstEventLogs;
import net.nodus.core.site.service.SiteActivationService.TimeToActivationLogs;
import net.nodus.database.sdk.SiteActivationLog;
import net.nodus.database.sdk.SiteVisitLog;

public class SiteActivationResponse {

    @Builder
    public record ActiveUserCountResponse(
        Long count
    ) {

        public static ActiveUserCountResponse from(List<SiteVisitLog> visitLogList) {
            Long count = visitLogList.stream()
                .map(SiteVisitLog::getSessionId)
                .distinct()
                .count();

            return ActiveUserCountResponse.builder()
                .count(count)
                .build();
        }
    }

    @Builder
    public record ActivationRateResponse(
        Long totalUserCount,
        Long activatedUserCount,
        Double rate
    ) {

        public static ActivationRateResponse from(
            List<SiteVisitLog> visitLogList,
            List<SiteActivationLog> activationLogList
        ) {
            long totalUserCount = visitLogList.stream()
                .map(SiteVisitLog::getSessionId)
                .distinct()
                .count();

            long activatedUserCount = activationLogList.stream()
                .map(SiteActivationLog::getSessionId)
                .distinct()
                .count();

            return ActivationRateResponse.builder()
                .totalUserCount(totalUserCount)
                .activatedUserCount(activatedUserCount)
                .rate(calculateRate(activatedUserCount, totalUserCount))
                .build();
        }
    }

    @Builder
    public record TimeToActivationResponse(
        Long averageSeconds
    ) {

        public static TimeToActivationResponse from(TimeToActivationLogs logs) {
            Map<String, LocalDateTime> firstVisitTimeBySessionId = findFirstTimeBySessionId(
                logs.visitLogs(),
                SiteVisitLog::getSessionId,
                SiteVisitLog::getCreatedAt
            );
            Map<String, LocalDateTime> firstActivationTimeBySessionId = findFirstTimeBySessionId(
                logs.activationLogs(),
                SiteActivationLog::getSessionId,
                SiteActivationLog::getCreatedAt
            );

            return TimeToActivationResponse.builder()
                .averageSeconds(calculateAverageTimeToActivationSeconds(
                    firstVisitTimeBySessionId,
                    firstActivationTimeBySessionId
                ))
                .build();
        }
    }

    @Builder
    public record FirstEventCountResponse(
        Long count
    ) {

        public static FirstEventCountResponse from(FirstEventLogs logs) {
            Map<String, LocalDateTime> firstVisitTimeBySessionId = findFirstTimeBySessionId(
                logs.visitLogs(),
                SiteVisitLog::getSessionId,
                SiteVisitLog::getCreatedAt
            );
            Map<String, LocalDateTime> firstActivationTimeBySessionId = findFirstTimeBySessionId(
                logs.activationLogs(),
                SiteActivationLog::getSessionId,
                SiteActivationLog::getCreatedAt
            );

            return FirstEventCountResponse.builder()
                .count(countFirstActivations(firstVisitTimeBySessionId,
                    firstActivationTimeBySessionId))
                .build();
        }
    }

    private static <T> long countDistinctSessionIds(
        List<T> logs,
        Function<T, String> sessionIdGetter
    ) {
        return logs.stream()
            .map(sessionIdGetter)
            .filter(SiteActivationResponse::isValidSessionId)
            .distinct()
            .count();
    }

    private static <T> Map<String, LocalDateTime> findFirstTimeBySessionId(
        List<T> logs,
        Function<T, String> sessionIdGetter,
        Function<T, LocalDateTime> createdAtGetter
    ) {
        return logs.stream()
            .filter(log -> isValidSessionId(sessionIdGetter.apply(log)))
            .filter(log -> createdAtGetter.apply(log) != null)
            .collect(Collectors.toMap(
                sessionIdGetter,
                createdAtGetter,
                (first, second) -> first.isBefore(second) ? first : second
            ));
    }

    private static boolean isValidSessionId(String sessionId) {
        return sessionId != null && !sessionId.isBlank();
    }

    private static long countFirstActivations(
        Map<String, LocalDateTime> firstVisitTimeBySessionId,
        Map<String, LocalDateTime> firstActivationTimeBySessionId
    ) {
        return firstActivationTimeBySessionId.entrySet().stream()
            .filter(entry -> firstVisitTimeBySessionId.containsKey(entry.getKey()))
            .filter(
                entry -> !entry.getValue().isBefore(firstVisitTimeBySessionId.get(entry.getKey())))
            .count();
    }

    private static long calculateAverageTimeToActivationSeconds(
        Map<String, LocalDateTime> firstVisitTimeBySessionId,
        Map<String, LocalDateTime> firstActivationTimeBySessionId
    ) {
        return Math.round(
            firstActivationTimeBySessionId.entrySet().stream()
                .filter(entry -> firstVisitTimeBySessionId.containsKey(entry.getKey()))
                .filter(entry -> !entry.getValue()
                    .isBefore(firstVisitTimeBySessionId.get(entry.getKey())))
                .mapToLong(entry -> Duration.between(
                    firstVisitTimeBySessionId.get(entry.getKey()),
                    entry.getValue()
                ).getSeconds())
                .average()
                .orElse(0)
        );
    }

    private static double calculateRate(long targetCount, long totalCount) {
        if (totalCount == 0) {
            return 0;
        }
        double rate = (double) targetCount / totalCount * 100;
        return Math.round(rate * 10.0) / 10.0;
    }
}
