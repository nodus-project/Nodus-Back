package net.nodus.common

import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

abstract class MutableDocument : BaseDocument() {
    @LastModifiedDate
    var updatedAt: Instant = Instant.now()

    fun touch(now: Instant = Instant.now()) {
        updatedAt = now
    }

    override fun softDelete(now: Instant) {
        deletedAt = now
        updatedAt = now
    }

    override fun restore(now: Instant) {
        deletedAt = null
        updatedAt = now
    }
}