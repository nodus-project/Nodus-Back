package net.nodus.site.dto

import jakarta.validation.constraints.NotBlank

data class CreateSiteRequest(
    @field:NotBlank
    val name: String,

    val domain: String? = null,

    val url: String? = null,
)

data class UpdateSiteRequest(
    val name: String? = null,
    val domain: String? = null,
    val url: String? = null,
)
