package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Modell for svar fra Texas-introspeksjon.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasResponse(
    val active: Boolean,
    val error: String?
)
