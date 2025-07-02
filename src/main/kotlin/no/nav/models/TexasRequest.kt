package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.serialization.Serializable

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasRequest(
    val identity_provider: String,
    val token: String
)

