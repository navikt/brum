package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.models.UkeAntallRecord

class UkeAntallService() {
    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
    val queryRunner = BigQueryRunner()

    fun ukeAntallRecords(
        prosjektId: String,
        år: Int,
        uke: Int
    ): String {
        val sql = """
      SELECT
        `år`, `uke`, `tiltaksnavn`, `innsatsgruppe`, `avdeling`, `antall`
      FROM `brum-dev-b72f.tiltak_gold.uke_antall_gold_mock`
      WHERE `år` = $år AND `uke` = $uke
      LIMIT 10
    """.trimIndent()

        val rows = queryRunner.runQuery(sql, prosjektId)
        val records = rows.map { row ->
            UkeAntallRecord(
                aar = row.get("år").numericValue.toInt(),
                uke = row.get("uke").numericValue.toInt(),
                tiltaksnavn = row.get("tiltaksnavn").stringValue,
                innsatsgruppe = row.get("innsatsgruppe").stringValue,
                avdeling = row.get("avdeling").stringValue,
                antall = row.get("antall").numericValue.toLong()
            )
        }
        return jacksonObjectMapper().writeValueAsString(records)
    }
}