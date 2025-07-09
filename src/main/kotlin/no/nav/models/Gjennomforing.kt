package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import java.time.LocalDate

/**
 * Modell for en gjennomføring.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GjennomforingRespons(
    val gjennomforingId: String,
    val navn: String,
    val startDato: LocalDate,
    val sluttDato: LocalDate?
)
