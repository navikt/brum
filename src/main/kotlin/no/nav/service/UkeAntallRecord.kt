package no.nav.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.models.AvdelingOnlyData
import no.nav.models.UkeAntallInnsatsRecord
import no.nav.models.TransformedData
import no.nav.models.AvdelingsData
import no.nav.models.UkeAntallAvdelingRecord

class UkeAntallService() {
    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
    val queryRunner = BigQueryRunner()

    fun ukeAntallRecords(
        prosjektId: String,
        aar: Int,
        uke: Int
    ): String {
        val sqlInnsats = """
      SELECT
        `år`, `uke`, `tiltaksnavn`, `innsatsgruppe`, `avdeling`, `antall`
      FROM `brum-prod-6e57.tiltak_gold.uke_antall_gold_mock`
      WHERE `år` = $aar AND `uke` = $uke
    """.trimIndent()
        val sqlAvdeling = """
      SELECT
        `år`, `uke`, `tiltaksnavn`,`avdeling`, `antall`
      FROM `brum-prod-6e57.tiltak_gold.uke_antall_avdeling_gold_mock`
      WHERE `år` = $aar AND `uke` = $uke
    """.trimIndent()

        // Vi henter både den fulle tabellen med innsatsgrupper og avdeling
        // og den med bare avdeligner, for å gi en effektiv mapping mellom begge
        val rowsInnsats = queryRunner.runQuery(sqlInnsats, prosjektId)
        val recordsInnsats = rowsInnsats.map { row ->
            UkeAntallInnsatsRecord(
                aar = row.get("år").numericValue.toInt(),
                uke = row.get("uke").numericValue.toInt(),
                tiltaksnavn = row.get("tiltaksnavn").stringValue,
                innsatsgruppe = row.get("innsatsgruppe").stringValue,
                avdeling = row.get("avdeling").stringValue,
                antall = row.get("antall").numericValue.toLong()
            )
        }
        val rowsAvdeling = queryRunner.runQuery(sqlAvdeling, prosjektId)
        val recordsAvdeling = rowsAvdeling.map { row ->
            UkeAntallAvdelingRecord(
                aar = row.get("år").numericValue.toInt(),
                uke = row.get("uke").numericValue.toInt(),
                tiltaksnavn = row.get("tiltaksnavn").stringValue,
                avdeling = row.get("avdeling").stringValue,
                antall = row.get("antall").numericValue.toLong()
            )
        }

        val transformed = formaterData(recordsInnsats, recordsAvdeling)
        return jacksonObjectMapper().writeValueAsString(transformed)
    }

    fun formaterData(recordsInnsats: List<UkeAntallInnsatsRecord>, recordsAvdeling: List<UkeAntallAvdelingRecord>):  TransformedData {
        // Trenger ikke egentlig år og uke siden de alltid er like for en SQL spørring
        // Nyttig metadata for testing
        val aar = recordsInnsats.firstOrNull()?.aar ?: 0
        val uke = recordsInnsats.firstOrNull()?.uke ?: 0

        // Dynamisk hent bare de tiltaksnavnene som er tilstede i dataen
        val headers = recordsInnsats
            .map { it.tiltaksnavn }
            .distinct()
            .sorted()

        // Grupper dataen ved både avdeling og innsatsgruppe ( serie og stack)
        val grouped = recordsInnsats.groupBy { Pair(it.avdeling, it.innsatsgruppe) }

        // Formater dataen til å ha keys som er den gitte metadataen
        // Rader er de forskjellige verdiene til en gitt innsattsgruppe under en gitt avdeling
        val dataInnsats = grouped.map { (key, groupRecords) ->
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
        }.sortedWith(
            compareBy<AvdelingsData> { it.avdeling }
                .thenBy { it.innsatsgruppe }
        )

        val groupedAvdeling = recordsAvdeling.groupBy { it.avdeling }

        val dataAvdeling = groupedAvdeling.map { (avdeling, groupRecords) ->
            val verdier = headers.map { header ->
                groupRecords
                    .filter { it.tiltaksnavn.equals(header, ignoreCase = true) }
                    .sumOf { it.antall }
            }

            AvdelingOnlyData(
                avdeling = avdeling,
                verdier = verdier
            )
        }.sortedBy { it.avdeling }

        return TransformedData(
            aar = aar,
            uke = uke,
            headers = headers,
            data = dataInnsats,
            dataAvdeling = dataAvdeling
        )
    }

    /*fun jsonToCsv(json: String): ByteArray {
        val mapper = jacksonObjectMapper()
        val records: List<UkeAntallRecord> = mapper.readValue(json)

        val csvMapper = CsvMapper()
        val schema: CsvSchema = csvMapper
            .schemaFor(UkeAntallRecord::class.java)
            .withHeader()


        val csvString: String = csvMapper.writer(schema)
            .writeValueAsString(records)

        return csvString.toByteArray(Charsets.UTF_8)
    }*/
}