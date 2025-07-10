package no.nav.api.gjennomforing

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.config.Environment
import no.nav.logger
import no.nav.models.FeilRespons
import no.nav.service.GjennomforingService


/**
 * Registrerer ruter for gjennomføringer under /gjennomforinger.
 */
fun Route.gjennomforingRuter() {
    // objekt for forretningslogikk knyttet til gjennomføringer
    val service = GjennomforingService()

    get("/gjennomforinger") {
        try {
            val json = service.hentGjennomforinger(envProjectId = "brum-dev-b72f")
            call.respond(HttpStatusCode.OK, json)
        } catch (e: Exception) {
            logger.error("Feil ved /gjennomforinger ${call.request.uri}", e)
            call.respond(HttpStatusCode.InternalServerError, FeilRespons("Feil ved henting av gjennomføringer: ${e.message}"))
        }
    }

}
