package no.nav.service

import com.google.cloud.bigquery.*
import java.util.UUID


/**
 * Utfører spørringer mot Google BigQuery.
 */
class BigQueryRunner() {
    /**
     * Kjører en spørring mot BigQuery
     *
     * @param sql       SQL-spørringen.
     * @param prosjekt  Google Cloud-prosjekt-ID.
     * @return Iterator over resultatrader.
     * */
    fun runQuery(query: String, prosjekt: String): Iterable<FieldValueList>{
        val bigQuery = BigQueryOptions.getDefaultInstance().service

        val config = QueryJobConfiguration.newBuilder(query)
            .setUseLegacySql(false)
            .build()

        val jobId = JobId.of(prosjekt, UUID.randomUUID().toString())
        val jobInfo = JobInfo.newBuilder(config).setJobId(jobId).build()
        val job = bigQuery.create(jobInfo)

        if (job == null || job.status.error != null) {
            throw RuntimeException("Query failed: ${job?.status?.error}")
        }
        return job.getQueryResults().iterateAll()
    }
}

