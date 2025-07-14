package no.nav.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.models.UkeAntallRecord
import no.nav.models.TransformedData
import no.nav.models.AvdelingsData

class UkeAntallService() {
    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
    val queryRunner = BigQueryRunner()

    fun ukeAntallRecords(
        prosjektId: String,
        aar: Int,
        uke: Int
    ): String {
        val sql = """
      SELECT
        `år`, `uke`, `tiltaksnavn`, `innsatsgruppe`, `avdeling`, `antall`
      FROM `brum-prod-6e57.tiltak_gold.uke_antall_gold_mock`
      WHERE `år` = $aar AND `uke` = $uke
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
        val transformed = formaterData(records)
        return jacksonObjectMapper().writeValueAsString(transformed)
    }

    fun formaterData(records: List<UkeAntallRecord>):  TransformedData {
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
        val grouped = records.groupBy { Pair(it.avdeling, it.innsatsgruppe) }

        val data = grouped.map { (key, groupRecords) ->
            val (avdeling, innsatsgruppe) = key
            val verdier = headers.map { header ->
                groupRecords
                    .find { it.tiltaksnavn.equals(header, ignoreCase = true) }
                    ?.antall ?: 0L
            }

            AvdelingsData(
                avdeling = avdeling,
                innsatsgruppe = innsatsgruppe,
                verdier = verdier,
            )
        }

        return TransformedData(
            aar = aar,
            uke = uke,
            headers = headers,
            data = data
        )
    }


    fun jsonToCsv(json: String): ByteArray {
        val mapper = jacksonObjectMapper()
        val records: List<UkeAntallRecord> = mapper.readValue(json)

        val csvMapper = CsvMapper()
        val schema: CsvSchema = csvMapper
            .schemaFor(UkeAntallRecord::class.java)
            .withHeader()


        val csvString: String = csvMapper.writer(schema)
            .writeValueAsString(records)

        return csvString.toByteArray(Charsets.UTF_8)
    }
}