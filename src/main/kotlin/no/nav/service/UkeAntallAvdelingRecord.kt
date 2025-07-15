package no.nav.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.models.UkeAntallAvdelingRecord
import no.nav.models.TransformedAvdelingData
import no.nav.models.AvdelingOnlyData

class UkeAntallAvdelingService() {
    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
    val queryRunner = BigQueryRunner()

    fun ukeAntallAvdelingRecords(
        prosjektId: String,
        aar: Int,
        uke: Int
    ): String {
        val sql = """
      SELECT
        `år`, `uke`, `tiltaksnavn`, `avdeling`, `antall`
      FROM `brum-prod-6e57.tiltak_gold.uke_antall_avdeling_gold_mock`
      WHERE `år` = $aar AND `uke` = $uke
    """.trimIndent()

        val rows = queryRunner.runQuery(sql, prosjektId)
        val records = rows.map { row ->
            UkeAntallAvdelingRecord(
                aar = row.get("år").numericValue.toInt(),
                uke = row.get("uke").numericValue.toInt(),
                tiltaksnavn = row.get("tiltaksnavn").stringValue,
                avdeling = row.get("avdeling").stringValue,
                antall = row.get("antall").numericValue.toLong()
            )
        }
        val transformed = formaterData(records)
        return jacksonObjectMapper().writeValueAsString(transformed)
    }

    fun formaterData(records: List<UkeAntallAvdelingRecord>):  TransformedAvdelingData {
        // Trenger ikke egentlig år og uke siden de alltid er like for en SQL spørring
        // Nyttig metadata for testing
        val aar = records.firstOrNull()?.aar ?: 0
        val uke = records.firstOrNull()?.uke ?: 0

        // Dynamisk hent bare de tiltaksnavnene som er tilstede i dataen
        val headers = records
            .map { it.tiltaksnavn }
            .distinct()
            .sorted()

        // Grupper dataen ved både avdeling og innsatsgruppe ( serie og stack)
        val grouped = records.groupBy { it.avdeling}

        // Formater dataen til å ha keys som er den gitte metadataen
        // Rader er de forskjellige verdiene til en gitt innsattsgruppe under en gitt avdeling
        val data = grouped.map { (key, groupRecords) ->
            val avdeling = key
            val verdier = headers.map { header ->
                groupRecords
                    .find { it.tiltaksnavn.equals(header, ignoreCase = true) }
                    ?.antall ?: 0L
            }

            AvdelingOnlyData(
                avdeling = avdeling,
                verdier = verdier,
            )
        }.sortedWith(
            compareBy { it.avdeling }
        )

        return TransformedAvdelingData(
            aar = aar,
            uke = uke,
            headers = headers,
            data = data
        )
    }
}