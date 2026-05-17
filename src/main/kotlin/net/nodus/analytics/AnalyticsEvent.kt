package net.nodus.analytics


import net.nodus.common.BaseDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("analytics_events")
class AnalyticsEvent (
    @Id
    val id: String? = null,

    @Indexed
    val siteKeyId: String,

    @Indexed
    val userAccountId: String,

    @Indexed
    val siteId: String,

    @Indexed
    val sessionId: String,

    @Indexed
    val eventType: String,

    @Indexed
    val eventName: String,

    val baseUrl: String?,
    val lastPage: String?,
    val currentPage: String?,

    val referrerType: ReferrerType? = null,

    var durationMs: Long? = null,

    val properties: Map<String, Any?> = emptyMap(),

    @Indexed
    val eventTime: Instant,
) : BaseDocument()