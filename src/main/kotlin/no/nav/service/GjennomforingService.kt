package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.models.GjennomforingRespons
import java.time.Instant
import java.time.LocalDate

class GjennomforingService(
    private val queryRunner: BigQueryRunner = BigQueryRunner()
) {
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
                opprettetTidspunkt = Instant.ofEpochSecond(
                    row["opprettet_tidspunkt"].doubleValue.toLong(),
                    (((row["opprettet_tidspunkt"].doubleValue % 1) * 1_000_000_000).toLong())
                ),
                oppdatertTidspunkt = Instant.ofEpochSecond(
                    row["oppdatert_tidspunkt"].doubleValue.toLong(),
                    (((row["oppdatert_tidspunkt"].doubleValue % 1) * 1_000_000_000).toLong())
                ),
                avsluttetTidspunkt = row["avsluttet_tidspunkt"].takeIf { !it.isNull }?.doubleValue?.let {
                    Instant.ofEpochSecond(
                        it.toLong(),
                        (((it % 1) * 1_000_000_000).toLong())
                    )
                }
            )
        }

        return jacksonObjectMapper().writeValueAsString(list)
    }
}
