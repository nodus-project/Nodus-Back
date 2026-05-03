package net.nodus.account

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "client_keys")
class ClientKey(
    @Id
    val id: String? = null,

    @Indexed
    val userAccountId: String,

    @Indexed(unique = true)
    val key: String,

    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now(),
)
