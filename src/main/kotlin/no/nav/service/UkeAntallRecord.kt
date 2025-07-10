package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.models.UkeAntallRecord

class UkeAntallRecord (){
    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
    private val queryRunner: BigQueryRunner = BigQueryRunner()

    fun hentUkeAntallRecord(envProjectId: String, arr: String, uke: String): String {
        return UkeAntallRecordFraBigQuery(envProjectId, arr, uke)
    }
        /**
         * Henter siste 10 gjennomføringer som JSON.
         */
        fun UkeAntallRecordFraBigQuery(prosjektId: String, arr: String, ukeNr: String): String {
            val sql = """
                SELECT
                år,
                uke,
                tiltaksnavn,
                innsatsgruppe,
                avdeling,
                antall
                FROM `${prosjektId}.${dataNiva}`
                WHERE år = @ar AND uke = @uke
                LIMIT 10
                """.trimIndent()

            val rader = queryRunner.runQuery(sql, prosjektId)
            val list = rader.map { row ->
                UkeAntallRecord(
                    ar            = row.get("år").numericValue.toInt(),
                    uke           = row.get("uke").numericValue.toInt(),
                    tiltaksnavn   = row.get("tiltaksnavn").stringValue,
                    innsatsgruppe = row.get("innsatsgruppe").stringValue,
                    avdeling      = row.get("avdeling").stringValue,
                    antall        = row.get("antall").numericValue.toLong()
                )
            }

            return jacksonObjectMapper().writeValueAsString(list)
        }
}