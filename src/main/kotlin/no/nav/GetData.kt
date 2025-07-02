package no.nav

fun getGjennomforinger(prosjektId: String): String {
    val query = """
        SELECT *
        FROM `brum-dev-b72f.tiltak_bronze.gjennomforinger_bronze`
        LIMIT 10
    """.trimIndent()
    val results = runBigQuery(query, prosjektId)
    return results.toString()
}