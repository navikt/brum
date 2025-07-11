package no.nav.api.UkeAntallRecord

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.*
import no.nav.logger
import no.nav.models.FeilRespons
import no.nav.service.BigQueryRunner
import no.nav.service.UkeAntallService

fun Route.ukeAntallRecordsRoute() {
    get("/ukeAntall") {
        val service = UkeAntallService()
        val aarParam = call.request.queryParameters["aar"]
        val ukeParam = call.request.queryParameters["uke"]

        val aar = aarParam?.toIntOrNull()
            ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                FeilRespons("Ugyldig eller manglende år: $aarParam")
            )
        val uke = ukeParam?.toIntOrNull()
            ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                FeilRespons("Ugyldig eller manglende uke: $ukeParam")
            )

        try {
            val json = service.ukeAntallRecords("brum-dev-b72f", aar, uke)
            val csvBytes = service.jsonToCsv(json)
            call.respondBytes(
                bytes = csvBytes,
                contentType = ContentType.Text.CSV.withCharset(Charsets.UTF_8),
                status = HttpStatusCode.OK
            )
        } catch (e: Exception) {
            logger.error("Feil ved /ukeAntall?aar=$aar&uke=$uke", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                FeilRespons("Feil ved henting av ukeAntallData: ${e.message}")
            )
        }
    }
}