package net.nodus.core.sdk.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import net.nodus.database.sdk.TrafficChannel;

public class SdkLogRequest {

    public record SdkVisitPostRequest(
        @NotNull
        String sessionId,

        @NotNull
        String lastPage,

        @NotBlank
        String currentPage
    ) {

    }

    public record SdkActivationPostRequest(
        @NotNull
        String sessionId,

        @NotNull
        String featureName,

        @NotBlank
        String currentPage
    ) {

    }

    public record SdkRevenuePostRequest(
        @NotNull
        String sessionId,

        String tag,

        @NotBlank
        String currentPage
    ) {

    }

    public record SdkTrafficSourcePostRequest(
        @NotNull
        UUID sessionId,

        @NotNull
        TrafficChannel channel,

        String source,

        String medium,

        String campaign,

        String term,

        String content,

        String referrer,

        String referrerHost,

        String landingPage,

        String landingPath,

        String clickId
    ) {

    }
}
