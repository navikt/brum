package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class UkeAntallRecord(
    val aar: Int,
    val uke: Int,
    val tiltaksnavn: String,
    val innsatsgruppe: String,
    val avdeling: String,
    val antall: Long
)
