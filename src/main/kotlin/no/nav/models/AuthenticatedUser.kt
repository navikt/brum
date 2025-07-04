package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.serialization.Serializable

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticatedUser(
    val oid: String,
    val username: String,
    val groups: List<String>,
    val azp: String? = null,
    val azp_name: String? = null,
    val NAVident: String? = null,
    val roles: List<String> = emptyList(),
    val scp: List<String> = emptyList(),
    val idtyp: String? = null

)