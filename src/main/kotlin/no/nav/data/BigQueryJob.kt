package no.nav.data

import com.google.cloud.bigquery.*
import java.util.*

fun runBigQuery(query: String, prosjektId: String): Iterable<FieldValueList> {
    val bigquery = BigQueryOptions.getDefaultInstance().service
    val queryConfig = QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build()
    val jobId = JobId.of(prosjektId, UUID.randomUUID().toString())
    val queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()
    if (queryJob == null || queryJob.status.error != null) {
        throw RuntimeException("Query failed: ${queryJob?.status?.error}")
    }
    return queryJob.getQueryResults().iterateAll()
}
