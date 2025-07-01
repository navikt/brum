package no.nav

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.staticFiles
import java.io.File

fun Application.configureRouting() {
    routing {   authenticate("auth-bearer") {
        get("/testAuth") {
            logger.info("ok")
            call.respond("ok")
        }
    }
        get("/gjennomforing") {
            logger.info("ok")
            //call.respond("ok")
            try {
                val result = getGjennomforinger("brum-dev-b72f")
                call.respondText(result, ContentType.Text.Plain)
            } catch (e: Exception) {
                logger.error("Feil ved henting av gjennomforing", e)
                call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av gjennomforing: ${e.message}")
            }
        }

        staticFiles("/testData", File("files"))

        get("/auth") {
            logger.info("Request received")
            call.respond(
                mapOf(
                    "active" to true
                )
            )
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

