package net.nodus.analytics

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class CreateAnalyticsEventRequest(
    @field:NotBlank
    val siteKey: String,

    @field:NotBlank
    val sessionId: String,

    @field:NotBlank
    val eventName: String,

    @field:NotBlank
    val eventType: String,

    @field:NotBlank
    @JsonProperty("base_url")
    @JsonAlias("baseUrl")
    val baseUrl: String,

    @JsonProperty("last_page")
    @JsonAlias("lastPage")
    val lastPage: String? = null,

    @field:NotBlank
    @JsonProperty("current_page")
    @JsonAlias("currentPage")
    val currentPage: String,

    val properties: Map<String, Any?> = emptyMap(),

    @field:NotNull
    val time: Instant,
)

data class AnalyticsEventResponse(
    val id: String,
    val siteId: String,
    val sessionId: String,
    val eventType: String,
    val eventName: String,
    val referrerType: ReferrerType?,
    val durationMs: Long?,
    val eventTime: String,
)

fun AnalyticsEvent.toResponse(): AnalyticsEventResponse =
    AnalyticsEventResponse(
        id = requireNotNull(id),
        siteId = siteId,
        sessionId = sessionId,
        eventType = eventType,
        eventName = eventName,
        referrerType = referrerType,
        durationMs = durationMs,
        eventTime = eventTime.toString(),
    )