package net.nodus.site.entity

import net.nodus.common.MutableDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("sites")
class Site (
    @Id val id: String? = null,

    @Indexed val userAccountId: String,

    var name: String,

    var domain: String? = null,

    var url: String? = null,
) : MutableDocument()
