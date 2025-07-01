package no.nav

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
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


        get("/testData") {
            logger.info("data requested")
            call.respond(getTestData())
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

                    logger.info("Authenticated user fetched for /userInfo: oid=${user.oid}, username=${user.username}, groups=${user.groups}")

                    call.respond(
                        HttpStatusCode.OK,
                        mapOf(
                            "oid" to user.oid,
                            "username" to user.username,
                            "groups" to user.groups
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

