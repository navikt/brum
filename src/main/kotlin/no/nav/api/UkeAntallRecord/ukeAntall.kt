package no.nav.api.UkeAntallRecord

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.*
import no.nav.logger
import no.nav.models.FeilRespons
import no.nav.service.UkeAntallRecord

fun Route.UkeAntallRecordRoute(){
    //authenticate("auth-bearer") {
        val service = UkeAntallRecord()

        get("/ukeAntall") {
            val årParam = call.request.queryParameters["ar"]
            val ukeParam = call.request.queryParameters["uke"]
            val ar = årParam?.toIntOrNull()
            val uke = ukeParam?.toIntOrNull()
            if (ar == null || uke == null) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    FeilRespons("Ugyldig eller manglende param: ar=$årParam, uke=$ukeParam")
                )
            }

            try {
                val json = service.hentUkeAntallRecord(envProjectId = "brum-dev-b72f", årParam, ukeParam)
                call.respond(HttpStatusCode.OK, json)
            } catch (e: Exception) {
                logger.error("Feil ved /ukeAntall?ar=$år&uke=$uke", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    FeilRespons("Feil ved henting av ukeAntallData: ${e.message}")
                )
            }
        }
    //}
}