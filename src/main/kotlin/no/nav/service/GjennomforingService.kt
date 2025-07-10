package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.config.Environment
import no.nav.models.GjennomforingRespons
import java.time.Instant
import java.time.LocalDate
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

class GjennomforingService{
    private val queryRunner: BigQueryRunner = BigQueryRunner()

    fun hentGjennomforinger(envProjectId: String): String {
    return hentGjennomforingerdata(envProjectId)
    }
    /**
     * Henter siste 10 gjennomføringer som JSON.
     */
    fun hentGjennomforingerdata(envProjectId: String): String {
        val sql = """
            SELECT *
            FROM `brum-dev-b72f.tiltak_silver.gjennomforinger_silver`
            LIMIT 10
        """.trimIndent()

        val rader = queryRunner.runQuery(sql, envProjectId)
        val list = rader.map { row ->
            GjennomforingRespons(
                gjennomforingId    = row["gjennomforing_id"].stringValue,
                navn               = row["navn"].stringValue,
                startDato          = LocalDate.parse(row["start_dato"].stringValue),
                sluttDato          = row["slutt_dato"].takeIf { !it.isNull }?.stringValue?.let(LocalDate::parse),
            )
        }



        val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        return mapper.writeValueAsString(list)
    }
}
