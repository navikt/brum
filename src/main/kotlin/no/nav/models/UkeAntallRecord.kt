package no.nav.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class UkeAntallInnsatsRecord(
    val aar: Int,
    val uke: Int,
    val tiltaksnavn: String,
    val innsatsgruppe: String,
    val avdeling: String,
    val antall: Long
)

data class UkeAntallAvdelingRecord(
    val aar: Int,
    val uke: Int,
    val tiltaksnavn: String,
    val avdeling: String,
    val antall: Long
)

data class TransformedData(
    val aar: Int,
    val uke: Int,
    val headers: List<String>,
    val data: List<AvdelingsData>,
    val dataAvdeling: List<AvdelingOnlyData>
)

data class AvdelingsData(
    val avdeling: String,
    val innsatsgruppe: String,
    val verdier: List<Long>
)

data class AvdelingOnlyData(
    val avdeling: String,
    val verdier: List<Long>
)


