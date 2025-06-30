package no.nav.models

import kotlinx.serialization.Serializable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasResponse(
    val active: Boolean,
    val error: String?,
)
