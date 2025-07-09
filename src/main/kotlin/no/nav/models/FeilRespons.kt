package no.nav.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Modell for feilmeldinger i API-respons.
 */
data class FeilRespons(
    @JsonProperty("errorMelding")
    val melding: String
)
