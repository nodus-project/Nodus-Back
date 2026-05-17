package net.nodus.analytics

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface AnalyticsEventRepository : MongoRepository<AnalyticsEvent, String> {
    fun findFirstBySiteIdAndSessionIdAndEventTypeAndEventTimeLessThanOrderByEventTimeDesc(
        siteId: String,
        sessionId: String,
        eventType: String,
        eventTime: Instant,
    ): AnalyticsEvent?
}