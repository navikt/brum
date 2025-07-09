package no.nav.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.models.GjennomforingRespons
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDate.parse

//class UkeAntall (){
//    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
//
//
//        /**
//         * Henter siste 10 gjennomføringer som JSON.
//         */
//        fun hentGjennomforinger(prosjektId: String): String {
//            val sql = """
//            SELECT *
//            FROM `${prosjektId}.${dataNiva}`
//            LIMIT 10
//        """.trimIndent()
//
//            val rader = queryRunner.runQuery(sql, prosjektId)
//            val list = rader.map { row ->
//            }
//
//            return jacksonObjectMapper().writeValueAsString(list)
//        }
//}