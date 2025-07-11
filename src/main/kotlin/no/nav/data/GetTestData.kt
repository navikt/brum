package no.nav.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private val objectMapper = jacksonObjectMapper()

fun getRealTestData(): String {
    return object {}::class.java.getResource("/testdata/real-test-data.csv").readText()
}

fun getMiniCsv(): Map<String, Array<Any>> {
    return objectMapper.readValue(object {}::class.java.getResource("/testdata/mini.json").readText());
}





























