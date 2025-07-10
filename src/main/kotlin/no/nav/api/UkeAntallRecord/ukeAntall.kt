package no.nav.api.UkeAntallRecord

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.*
import no.nav.logger
import no.nav.models.FeilRespons
import no.nav.service.BigQueryRunner
import no.nav.service.UkeAntallService

fun Route.ukeAntallRecordsRoute() {
    get("/ukeAntall") {
        val service = UkeAntallService()
        val årParam = call.request.queryParameters["ar"]
        val ukeParam = call.request.queryParameters["uke"]

        val år = årParam?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest,
                FeilRespons("Ugyldig eller manglende år: $årParam"))
        val uke = ukeParam?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest,
                FeilRespons("Ugyldig eller manglende uke: $ukeParam"))

        try {
            val json = service.ukeAntallRecords("brum-dev-b72f", år, uke)
            call.respond(HttpStatusCode.OK, json)
        } catch (e: Exception) {
            logger.error("Feil ved /ukeAntall?ar=$år&uke=$uke", e)
            call.respond(HttpStatusCode.InternalServerError,
                FeilRespons("Feil ved henting av ukeAntallData: ${e.message}"))
        }
    }
}