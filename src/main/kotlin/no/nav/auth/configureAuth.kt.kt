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
import com.auth0.jwt.JWT
import no.nav.config.Environment
import no.nav.logger
import no.nav.models.BrukerInfoRespons

/**
 * Installerer Bearer-autentisering som validerer token via Texas-sidecar.
 *
 * @param client HttpClient for å kommunisere med Texas-endepunktet.
 * @param env    Miljøvariabler, blant annet URL til Texas.
 */
fun Application.configureAuth(client: HttpClient, env: Environment) {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { credentials ->
                try {
                    val httpResponse = client.post(env.texasEndpoint) {
                        contentType(ContentType.Application.Json)
                        setBody(
                            TexasRequest(
                                identityProvider = "azuread",
                                token = credentials.token
                            )
                        )
                    }

                    if (!httpResponse.status.isSuccess()) {
                        val errorBody = httpResponse.body<String>()
                        logger.info("Texas introspeksjon feilet med HTTP-status ${httpResponse.status}: $errorBody")
                        return@authenticate null
                    }

                    val texasResponse = httpResponse.body<TexasResponse>()

                    if (texasResponse.error != null) {
                        logger.info("Token introspeksjon-feil: ${texasResponse.error}")
                        return@authenticate null
                    }

                    if (texasResponse.active) {
                        val jwt = JWT.decode(credentials.token)
                        val navIdent = jwt.getClaim("NAVident").asString() ?: "ukjent"
                        val email = jwt.getClaim("preferred_username").asString() ?: "ukjent"
                        val name = jwt.getClaim("name").asString() ?: "ukjent"

                        logger.info("Brukeren er autentisert: $navIdent")
                        return@authenticate BrukerInfoRespons(navIdent, email, name)
                    } else {
                        logger.info("Token er ikke aktiv ifølge Texas introspeksjon.")
                        return@authenticate null
                    }
                } catch (e: Exception) {
                    logger.error("Unntak under introspeksjon av Texas-token: ${e.message}", e)
                    return@authenticate null
                }
            }
        }
    }
}
