package net.nodus.sessionlog

import net.nodus.common.BaseDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "session_logs")
class SessionLog (
    @Id
    val id: String? = null,

    @Indexed
    val siteKeyId: String,


    @Indexed
    val siteId: String,

    @Indexed
    val userAccountId: String,

    val userSession: String,
) : BaseDocument()
