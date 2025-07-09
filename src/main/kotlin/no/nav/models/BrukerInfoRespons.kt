
package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Modell for en autentisert bruker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BrukerInfoRespons(
    @JsonProperty("NAVident") val navIdent: String,
    val email: String,
    val name: String
)