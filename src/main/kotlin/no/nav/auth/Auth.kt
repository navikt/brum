package no.nav.auth

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
import no.nav.config.Environment
import no.nav.logger


fun Application.configureAuth(client: HttpClient, env: Environment) {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { credentials ->
                val response = client.post(env.texasEndpoint) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        TexasRequest(
                            identity_provider = "azuread",
                            token = credentials.token
                        )
                    )
                }.body<TexasResponse>()

                if (response.error != null) {
                    logger.error("Token error from Texas: ${response.error}")
                }
                if (response.active) {
                    val jwt = JWT.decode(credentials.token)
                    val navIdent = jwt.getClaim("NAVident").asString() ?: "unknown"
                    val username = jwt.getClaim("preferred_username").asString() ?: "unknown"
                    val name = jwt.getClaim("name").asString() ?: "unknown"
                    AuthenticatedUser(navIdent, username, name)
                } else {
                    null
                }
            }
        }
    }
}
