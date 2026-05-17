package net.nodus.account

import net.nodus.common.MutableDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "user_accounts")
@CompoundIndex(
    name = "provider_provider_id_unique",
    def = "{'provider': 1, 'providerId': 1}",
    unique = true
)
class UserAccount(
    @Id
    var id: String? = null,

    var email: String,

    var name: String,

    var provider: OAuthProvider,

    var providerId: String,
) : MutableDocument()