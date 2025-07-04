package no.nav.data

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest
import com.google.cloud.secretmanager.v1.SecretVersionName
import java.io.ByteArrayInputStream

fun createBQClient(
    projectId: String = "brum-dev-b72f",
    serviceAccount: String = "service-account-key",
    version: String = "latest"): BigQuery {
        val secretClient = SecretManagerServiceClient.create()
        val secretName = SecretVersionName.of(projectId, serviceAccount, version)
        val response = secretClient.accessSecretVersion(
            AccessSecretVersionRequest.newBuilder().setName((secretName).toString()).build()
        )
        val secretData = response.payload.data.toStringUtf8()

        val credentials = ByteArrayInputStream(secretData.toByteArray(Charsets.UTF_8)).use {
            ServiceAccountCredentials.fromStream(it)
        }

        // Create and return BigQuery client
        return BigQueryOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(credentials)
            .build()
            .service
    }
