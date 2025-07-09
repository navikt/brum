package no.nav.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class TestData(val Gjennomforingsgruppe: Int, val Landegruppe3: Int, val SBestemt: Int, val STilpasset: Int)

private val objectMapper = jacksonObjectMapper()

fun getTestData1(): Array<TestData> {
    val json = TestData::class.java.getResource("/testdata/testdata1.json").readText()
    return objectMapper.readValue(json)
}

fun getTestData2(): Array<TestData> {
    val json = TestData::class.java.getResource("/testdata/testdata2.json").readText()
    return objectMapper.readValue(json)
}

fun getNoBehov(): Array<TestData> {
    val json = TestData::class.java.getResource("/testdata/real_no_behov.json").readText()
    return objectMapper.readValue(json)
}












