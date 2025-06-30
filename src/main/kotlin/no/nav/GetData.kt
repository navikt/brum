package no.nav

import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.BigQuery

fun main() {
    //val projectId = "brum-dev-b72f"
    //val sa_key = "service-account-key"

    val client = createBQClient()
    val query = "SELECT * FROM `brum-dev-b72f.tiltak_bronze.gjennomforinger_bronze` LIMIT 5"
    val config = com.google.cloud.bigquery.QueryJobConfiguration.newBuilder(query).build()
    val result = client.query(config)

    for (row in result.iterateAll()) {
        println(row) // Just print the whole row
    }
}