package no.nav

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

val logger: Logger = LoggerFactory.getLogger("Main")

fun Application.module() {
    routing {
        get("/") {
            logger.info("Request received")
            call.respond(HttpStatusCode.OK, "Hello, World!")
        }
    }
}