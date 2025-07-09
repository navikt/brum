package no.nav.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.jackson.jackson

/**
 * Lager en HTTP-klient med Jackson.
 */
fun createHttpClient() : HttpClient {
    return HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson()
        }
    }
}