package no.nav.service

import com.google.cloud.bigquery.*
import no.nav.logger
import java.util.UUID


/**
 * Utfører spørringer mot Google BigQuery.
 */
class BigQueryRunner {
    /**
     * Kjører en spørring mot BigQuery
     *
     * @param query       SQL-spørringen.
     * @param prosjektId  Google Cloud-prosjekt-ID.
     * @return Iterator over resultatrader.
     */
    fun runQuery(query: String, prosjektId: String): Iterable<FieldValueList> {
        val bigquery = BigQueryOptions.newBuilder()
            .setProjectId(prosjektId)
            .build()
            .service

        val queryConfig = QueryJobConfiguration.newBuilder(query)
            .setUseLegacySql(false)
            .build()

        val jobId = JobId.of(prosjektId, UUID.randomUUID().toString())
        val queryJob = bigquery
            .create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build())
            .waitFor()

        if (queryJob == null || queryJob.status.error != null) {
            throw RuntimeException("Query failed: ${queryJob?.status?.error}")
        }
        val creds = BigQueryOptions.getDefaultInstance().credentials
//        logger.info("Running BigQuery as: ${creds.clientEmail}")
        return queryJob.getQueryResults().iterateAll()
    }
}

