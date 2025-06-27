package no.nav

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

