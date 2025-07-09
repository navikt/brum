//package no.nav.service
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import no.nav.config.Environment
//
//class MockService (){
//    private val dataNiva = "tiltak_gold.uke_antall_gold_mock"
//    private val queryRunner: BigQueryRunner = BigQueryRunner()
//
//    fun hentMockData(env: Environment, arr:String , uke:String): String {
//        return hentMockDatafraBig(env,arr,uke)
//    }
//        /**
//         * Henter siste 10 gjennomføringer som JSON.
//         */
//        fun hentMockDatafraBig(prosjektId: Environment, arr: String, ukeNr: String): String {
//            val sql = """
//            SELECT *
//            FROM `${prosjektId}.${dataNiva},
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