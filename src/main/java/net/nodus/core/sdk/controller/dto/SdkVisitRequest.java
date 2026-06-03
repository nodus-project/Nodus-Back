package net.nodus.core.sdk.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SdkVisitRequest {

    public record SdkVisitPostRequest(
        @NotNull
        String sessionId,

        @NotNull
        String lastPage,

        @NotBlank
        String currentPage
    ) {

    }
}
