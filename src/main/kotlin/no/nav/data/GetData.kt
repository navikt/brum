package no.nav.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.bigquery.FieldValueList
import java.time.LocalDate
import java.time.Instant
import java.time.LocalDateTime

fun getGjennomforinger(prosjektId: String): String {
    val query = """
        SELECT *
        FROM `brum-dev-b72f.tiltak_silver.gjennomforinger_silver`
        LIMIT 10
    """.trimIndent()
    val results = runBigQuery(query, prosjektId)
    val list = results.map { row ->
        mapOf(
            "gjennomforing_id" to row["gjennomforing_id"].stringValue,
            "navn" to row["navn"].stringValue,
            "start_dato" to LocalDate.parse(row["start_dato"].stringValue).toString(),
            "slutt_dato" to row["slutt_dato"]
                .takeIf { !it.isNull }
                ?.stringValue
                ?.let { LocalDate.parse(it).toString() },
            "opprettet_tidspunkt" to row["opprettet_tidspunkt"]
                .doubleValue
                .let { Instant.ofEpochSecond(it.toLong(), ((it % 1) * 1_000_000_000).toLong()).toString() },
            "oppdatert_tidspunkt" to row["oppdatert_tidspunkt"]
                .doubleValue
                .let { Instant.ofEpochSecond(it.toLong(), ((it % 1) * 1_000_000_000).toLong()).toString() },
            "avsluttet_tidspunkt" to row["avsluttet_tidspunkt"]
                .takeIf { !it.isNull }
                ?.doubleValue
                ?.let { Instant.ofEpochSecond(it.toLong(), ((it % 1) * 1_000_000_000).toLong()).toString() }
        )
    }
    val mapper = jacksonObjectMapper()
    return mapper.writeValueAsString(list)
}

fun getBQTable(prosjektId: String): String {
    val query = "placeholder"
    return "hei"
}

