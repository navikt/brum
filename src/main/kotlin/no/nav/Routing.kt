package no.nav

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.application.call
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.staticFiles
import no.nav.models.AuthenticatedUser

fun Application.configureRouting() {
    routing {
        authenticate("auth-bearer") {
            get("/testAuth") {
                logger.info("ok")
                call.respond("ok")
            }
        }

        get("/gjennomforing") {
            logger.info("ok")
            try {
                val result = getGjennomforinger("brum-dev-b72f")
                call.respondText(result, ContentType.Text.Plain)
            } catch (e: Exception) {
                logger.error("Feil ved henting av gjennomforing", e)
                call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av gjennomforing: ${e.message}")
            }
        }


        get("/testData") {
            logger.info("data requested")
            call.respond(getTestData())
        }



        authenticate("auth-bearer") {
            get("/userInfo") {
                val user = call.principal<AuthenticatedUser>() ?: error("No authenticated user")
                call.respond(
                    mapOf(
                        "oid" to user.oid,
                        "username" to user.username,
                        "groups" to user.groups
                    )
                )
            }
        }
    }
}

