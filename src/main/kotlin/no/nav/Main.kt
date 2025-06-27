package no.nav



import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>): Unit = EngineMain.main(args)

val logger = org.slf4j.LoggerFactory.getLogger("Main")

fun Application.module() {
    val client = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson()
        }
    }
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
    configureAuth(client, env)
    configureRouting()
}


