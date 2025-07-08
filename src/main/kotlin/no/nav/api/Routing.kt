package no.nav.api

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.request.uri
import no.nav.data.getGjennomforinger
import no.nav.data.getNoBehov
import no.nav.data.getTestData1
import no.nav.data.getTestData2
import no.nav.logger
import no.nav.models.AuthenticatedUser

fun Application.configureRouting() {
    routing {
        authenticate("auth-bearer") {
            get("/testData") {
                val dataset = call.request.queryParameters["dataset"]
                when (dataset) {
                    "Test1" -> call.respond(getTestData1())
                    "Test2" -> call.respond(getTestData2())
                    "No behov" -> call.respond(getNoBehov())
                    else -> call.respondText(
                        "Invalid dataset number. No such dataset: $dataset. Full request: ${call.request.uri}",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }
        }

        get("/user/{login}") {
            if (call.parameters["login"] == "admin") {
                // ...
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

        authenticate("auth-bearer") {
            get("/testAuth") {
                logger.info("ok")
                call.respond("ok")
            }
        }

        authenticate("auth-bearer") {
            get("/userInfo") {
                try {
                    val user = call.principal<AuthenticatedUser>()
                    if (user == null) {
                        logger.warn("No authenticated user found in principal for /userInfo request")
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Unauthorized: No authenticated user found")
                        )
                        return@get
                    }
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf(
                            "NAVident" to user.NAVident,
                            "email" to user.email,
                            "name" to user.name
                        )
                    )
                } catch (e: Exception) {
                    logger.error("Exception while handling /userInfo", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("error" to "Internal server error: ${e.message}")
                    )
                }
            }
        }

    }
}

