package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.config.Environment
import no.nav.models.GjennomforingRespons
import java.time.Instant
import java.time.LocalDate

class GjennomforingService{
    private val queryRunner: BigQueryRunner = BigQueryRunner()

    fun hentGjennomforinger(env: Environment){
    return hentGjennomforinger(env)
}
    /**
     * Henter siste 10 gjennomføringer som JSON.
     */
    fun hentGjennomforinger(prosjektId: String): String {
        val sql = """
            SELECT *
            FROM `brum-dev-b72f.tiltak_silver.gjennomforinger_silver`
            LIMIT 10
        """.trimIndent()

        val rader = queryRunner.runQuery(sql, prosjektId)
        val list = rader.map { row ->
            GjennomforingRespons(
                gjennomforingId    = row["gjennomforing_id"].stringValue,
                navn               = row["navn"].stringValue,
                startDato          = LocalDate.parse(row["start_dato"].stringValue),
                sluttDato          = row["slutt_dato"].takeIf { !it.isNull }?.stringValue?.let(LocalDate::parse),
            )
        }

        return jacksonObjectMapper().writeValueAsString(list)
    }
}
