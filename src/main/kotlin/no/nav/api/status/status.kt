package no.nav.api.status

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.models.StatusRespons
import no.nav.utils.tilOppetid
import java.lang.management.ManagementFactory
import kotlin.time.Duration.Companion.milliseconds

/**
 * Definerer /status-rute med gjeldende oppetid.
 * Brukes for tesitng lokalt
 */
fun Route.status() {
    get("/status") {
        val millis = ManagementFactory.getRuntimeMXBean().uptime
        val oppetid = millis.milliseconds.tilOppetid()

        call.respond(
            HttpStatusCode.OK,
            StatusRespons(status = "ok", oppetid = oppetid)
        )
    }
}