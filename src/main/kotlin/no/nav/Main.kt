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
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.auth.principal
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.security.Principal
import kotlinx.serialization.json.jsonPrimitive

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Serializable
@JsonIgnoreProperties(ignoreUnknown = true)
data class TexasResponse(
    val active: Boolean,
    val error: String?,
    val claims: JsonObject? = null
)

@Serializable
data class TexasRequest(
    val identity_provider: String,
    val token: String
)

data class UserClaimsPrincipal(val claims: JsonObject) : Principal {
    override fun getName(): String? {
        TODO("Not yet implemented")
    }
}

val logger: Logger = LoggerFactory.getLogger("Main")

fun Application.module() {
    val client = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson()
        }
    }

    val env = Environment()
    install(CORS) {
        allowHost("localhost:3000", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        exposeHeader("X-Another-Custom-Header")
        allowCredentials = true
    }
    install(Authentication){
        bearer("auth-bearer") {
            authenticate { credentials ->
                val response = client.post(env.texasEndpoint) {
                    contentType(ContentType.Application.Json)
                    setBody(TexasRequest(
                        "azuread",
                        credentials.token
                    ))
                }.body<TexasResponse>()
                if (response.error != null) {
                    logger.error("Token error from Texas: ${response.error}")
                }
                if (response.active && response.claims != null) {
                     UserClaimsPrincipal(response.claims)

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
        /*staticFiles(remotePath = "/staticFile", dir= File("files"))*/

        authenticate("auth-bearer") {
            get("/testAuth") {
                logger.info("ok")
                val file = File("files/test-data.csv")
                call.respondFile(file)
              }
            }
            get("/getTestData") {
                logger.info("Request received")
                call.respondText("""gjennomforingsgruppe,Landegruppe3,S.Bestemt,S.Tilpasset,Testklasse
0,0,2,3,1000
1,373,363,390,1000
2,358,370,397,1000
3,346,417,372,2000
4,386,373,351,3000
5,391,348,355,3000
6,354,368,389,0
7,330,364,401,0
8,347,388,341,0
9,401,360,362,0""")
            }
        get("/auth") {
            logger.info("Request received")
            call.respond(
                mapOf(
                    "active" to true
                )
            )
        }
            get("/api/user-info") {
                val principal = call.principal<UserClaimsPrincipal>()

                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, "No authenticated user claims found.")
                    return@get
                }

                val displayName = principal.claims["name"]?.jsonPrimitive?.content
                val preferredUsername = principal.claims["preferred_username"]?.jsonPrimitive?.content
                val email = principal.claims["email"]?.jsonPrimitive?.content
                val navIdent = principal.claims["NAVident"]?.jsonPrimitive?.content


                val userInfo: Map<String, String?> = mapOf(
                    "name" to displayName,
                    "preferredUsername" to preferredUsername,
                    "email" to email,
                    "navIdent" to navIdent
                )

                call.respond(userInfo)
            }
        }
    }