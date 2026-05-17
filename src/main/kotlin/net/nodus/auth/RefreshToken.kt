package net.nodus.auth

import net.nodus.common.MutableDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "refresh_tokens")
class RefreshToken(
    @Id
    val id: String? = null,

    @Indexed
    val userAccountId: String,

    @Indexed(unique = true)
    val tokenHash: String,

    val expiresAt: Instant,

    var revokedAt: Instant? = null,
) : MutableDocument()
