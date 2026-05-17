package net.nodus.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.index.Indexed
import java.time.Instant

abstract class BaseDocument (
    @CreatedDate
    var createdAt: Instant = Instant.now(),

    @Indexed
    var deletedAt: Instant? = null,
) {
    open fun softDelete(now: Instant = Instant.now()) {
        deletedAt = now
    }

    open fun restore(now: Instant = Instant.now()) {
        deletedAt = null
    }

    val isDeleted: Boolean
        get() = deletedAt != null
}