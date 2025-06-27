package no.nav

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer
import no.nav.model.TexasResponse
import no.nav.model.TexasRequest


fun Application.configureAuth(client: HttpClient, env: Environment) {
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
                if (response.active) {
                    UserIdPrincipal("jetbrains")
                } else {
                    null
                }
            }
        }
    }
}
