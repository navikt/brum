package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.serialization.Serializable

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticatedUser(
    val oid: String,
    val username: String,
    val groups: List<String>
)
