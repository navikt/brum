package no.nav

import io.ktor.http.HttpStatusCode
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.staticFiles
import kotlinx.serialization.json.Json
import no.nav.models.AuthenticatedUser

data class Test(val Gjennomforingsgruppe: Int, val Landegruppe3: Int, val SBestemt: Int, val STilpasset: Int)

fun Application.configureRouting() {
    routing {
        authenticate("auth-bearer") {
            get("/testAuth") {
                logger.info("ok")
                call.respond("ok")
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

        get("/testData") {
            logger.info("data requested")
            call.respond(getTestData())

        }
    }

}

