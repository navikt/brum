package no.nav

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import no.nav.models.TexasResponse
import no.nav.models.TexasRequest
import no.nav.models.AuthenticatedUser
import com.auth0.jwt.JWT


fun Application.configureAuth(client: HttpClient, env: Environment) {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { credentials ->
                val response = client.post(env.texasEndpoint) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        TexasRequest(
                            identityProvider = "azuread",
                            token = credentials.token
                        )
                    )
                }.body<TexasResponse>()

                if (response.error != null) {
                    logger.error("Token error from Texas: ${response.error}")
                }
                if (response.active) {
                    val jwt = JWT.decode(credentials.token)
                    val oid = jwt.getClaim("oid").asString() ?: "unknown"
                    val username = jwt.getClaim("preferred_username").asString() ?: "unknown"
                    val groups = jwt.getClaim("groups").asList(String::class.java) ?: emptyList()

                    logger.info("Authenticated user oid=$oid username=$username groups=$groups")

                    AuthenticatedUser(oid, username, groups)
                } else {
                    null
                }
            }
        }
    }
}
