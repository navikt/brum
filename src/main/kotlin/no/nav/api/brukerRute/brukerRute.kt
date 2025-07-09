package no.nav.api.brukerRute

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.models.BrukerInfoRespons
import no.nav.models.FeilRespons

/**
 * Definerer ruter for å hente informasjon om pålogget bruker.
 * Krever at innkommende kall har gyldig bearer-token.
 */
fun Route.brukerRuter() {
    authenticate("auth-bearer") {
        get("/bruker-info") {
            val bruker = call.principal<BrukerInfoRespons>()
                ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    FeilRespons("Uautorisert: Token mangler eller er ugyldig.")
                )

            call.respond(
                HttpStatusCode.OK,
                BrukerInfoRespons(bruker.navIdent, bruker.email, bruker.name)
            )
        }
    }
}

