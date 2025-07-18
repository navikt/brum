
package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Modell for forespørsler til Texas-introspeksjon.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasRequest(
    @JsonProperty("identity_provider") val identityProvider: String,
    val token: String
)