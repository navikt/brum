package no.nav

import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import no.nav.api.configureRouting
import no.nav.auth.configureAuth
import no.nav.config.Environment
import no.nav.config.createHttpClient
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

val logger = org.slf4j.LoggerFactory.getLogger("Main")

/**
 * Hovedfunksjonen for Ktor-applikasjonen.
 * Starter Ktor-serveren og konfigurerer moduler for autentisering, ruter og CORS.
 *
 * @param args Kommandolinjeargumenter
 */
fun Application.module() {
    val httpClient = createHttpClient()
    val env = Environment()

    install(io.ktor.server.plugins.cors.routing.CORS) {
        allowHost(env.brumFrontEndUrl, schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
    }

    install(ContentNegotiation) {
        jackson()
    }

    configureAuth(httpClient, env)
    configureRouting()
}


