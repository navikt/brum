package no.nav

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Serializable
data class Tiltaksdata(
    val vedtak : Array<Int?>,
    val opptak : Array<Int?>,
    val skippertak: Array<Int>
)

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasResponse(
    val active: Boolean,
    val error: String?
)

val logger: Logger = LoggerFactory.getLogger("Main")

fun Application.module() {
    val client = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson()
        }
    }
    val env = Environment()

    install(Authentication){
        bearer("auth-bearer") {
            authenticate { credentials ->
                // send request to texas
                logger.info("Sending off ${credentials.token}")
                val response = client.post(env.texasEndpoint) {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf(
                        "identity_provider" to "azuread",
                        "token" to credentials.token
                    ))
                }.body<TexasResponse>()
                if (response.error != null) {
                    logger.error("Token error from Texas: ${response.error}")
                }
                if (response.active) {
                    UserIdPrincipal("jetbrains")
                } else {
                    null
                }
            }
        }
    }
    install(ContentNegotiation) {
        jackson()
    }
    routing {
        authenticate("auth-bearer") {
            get("/") {
                logger.info("Request received")
                call.respond(
                    HttpStatusCode.OK,
                    "brrrrrrrrrrrrrrum!!"
                )
            }
        }
        get("/auth") {
            logger.info("Request received")
            call.respond(
                mapOf(
                    "active" to true
                )
            )
        }
    }
}