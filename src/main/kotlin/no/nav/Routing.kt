package no.nav

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*

fun Application.configureRouting() {
    routing {
        /*staticFiles(remotePath = "/staticFile", dir= File("files"))*/
        authenticate("auth-bearer") {
            get("/testAuth") {
                logger.info("ok")
                call.respondText(
                    """gjennomforingsgruppe,Landegruppe3,S.Bestemt,S.Tilpasset,Testklasse
0,0,2,3,1000
1,373,363,390,1000
2,358,370,397,1000
3,346,417,372,2000
4,386,373,351,3000
5,391,348,355,3000
6,354,368,389,0
7,330,364,401,0
8,347,388,341,0
9,401,360,362,0"""
                )
            }
            get("/getTestData") {
                logger.info("Request received")
                call.respondText(
                    """gjennomforingsgruppe,Landegruppe3,S.Bestemt,S.Tilpasset,Testklasse
0,0,2,3,1000
1,373,363,390,1000
2,358,370,397,1000
3,346,417,372,2000
4,386,373,351,3000
5,391,348,355,3000
6,354,368,389,0
7,330,364,401,0
8,347,388,341,0
9,401,360,362,0"""
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
}
