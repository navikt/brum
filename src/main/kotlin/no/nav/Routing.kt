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

        get("/staus") {
            logger.info("ok")
            call.respond(HttpStatusCode.OK, "ok")
        }

//        staticFiles("/testData", File("files"))

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

