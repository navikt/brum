package no.nav.data

fun getRealTestData(): String {
    return object {}::class.java.getResource("/testdata/real-test-data.csv").readText()
}

fun getMiniCsv(): String {
    return object {}::class.java.getResource("/testdata/mini.csv").readText();
}





























