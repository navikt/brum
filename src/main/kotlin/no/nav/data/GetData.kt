package no.nav.data

fun getGjennomforinger(prosjektId: String): String {
    val query = """
        SELECT *
        FROM `brum-dev-b72f.tiltak_bronze.gjennomforinger_bronze`
        LIMIT 10
    """.trimIndent()
    val results = runBigQuery(query, prosjektId)
    println(results)
    return results.toString()
}
