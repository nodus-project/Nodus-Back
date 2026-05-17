package net.nodus.analytics

import net.nodus.site.SiteKeyService
import org.springframework.stereotype.Service
import java.net.URI
import java.time.Duration
import java.time.Instant

@Service
class AnalyticsEventService(
    private val siteKeyService: SiteKeyService,
    private val analyticsEventRepository: AnalyticsEventRepository,
) {
    fun create(request: CreateAnalyticsEventRequest): AnalyticsEvent {
        val siteKey = siteKeyService.authenticate(request.siteKey)

        if(request.eventType == AnalyticsEventTypes.PAGE_VIEW) {
            updatePreviousPageDuration(
                siteId = siteKey.siteId,
                sessionId = request.sessionId,
                currentEventTime = request.time
            )
        }

        return analyticsEventRepository.save(
            AnalyticsEvent(
                siteKeyId = requireNotNull(siteKey.id),
                userAccountId = siteKey.userAccountId,
                siteId = siteKey.siteId,
                sessionId = request.sessionId,
                eventType = request.eventType,
                eventName = request.eventName,
                baseUrl = request.baseUrl,
                lastPage = request.lastPage,
                currentPage = request.currentPage,
                referrerType = classifyReferrer(
                    baseUrl = request.baseUrl,
                    lastPage = request.lastPage,
                ),
                properties = request.properties,
                eventTime = request.time,
            )
        )
    }

    private fun updatePreviousPageDuration(
        siteId: String,
        sessionId: String,
        currentEventTime: Instant
    ) {
        val previousEvent =
            analyticsEventRepository.findFirstBySiteIdAndSessionIdAndEventTypeAndEventTimeLessThanOrderByEventTimeDesc(
                siteId,
                sessionId,
                eventType = AnalyticsEventTypes.PAGE_VIEW,
                eventTime = currentEventTime
            ) ?: return

        val durationMs = Duration.between(previousEvent.eventTime, currentEventTime).toMillis()

        if(durationMs > 0) {
            previousEvent.durationMs = durationMs
            analyticsEventRepository.save(previousEvent)
        }
    }

    private fun classifyReferrer(baseUrl: String, lastPage: String?): ReferrerType {
        if(lastPage.isNullOrBlank()) {
            return ReferrerType.DIRECT
        }

        val baseHost = parseHost(baseUrl) ?: return ReferrerType.UNKNOWN
        val lastHost = parseHost(lastPage) ?: return ReferrerType.UNKNOWN

        return if (isSameSite(baseHost, lastHost)) {
            ReferrerType.INTERNAL
        } else {
            ReferrerType.EXTERNAL
        }
    }

    private fun parseHost(url: String): String? =
        runCatching { URI(url).host }
            .getOrNull()
            ?.removePrefix("www.")
            ?.lowercase()

    private fun isSameSite(baseHost: String, targetHost: String): Boolean =
        targetHost == baseHost ||
                targetHost.endsWith(".$baseHost") ||
                baseHost.endsWith(".$targetHost")
}