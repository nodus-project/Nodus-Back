package net.nodus.core.analytics.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnalyticsEventRequest {

    @NotBlank
    private String siteKey;

    @NotBlank
    private String sessionId;

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventType;

    @NotBlank
    @JsonProperty("base_url")
    @JsonAlias("baseUrl")
    private String baseUrl;

    @JsonProperty("last_page")
    @JsonAlias("lastPage")
    private String lastPage;

    @NotBlank
    @JsonProperty("current_page")
    @JsonAlias("currentPage")
    private String currentPage;

    private Map<String, Object> properties = Map.of();

    @NotNull
    private Instant time;
}
