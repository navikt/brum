package no.nav.api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import no.nav.api.UkeAntallRecord.ukeAntallRecordsRoute
import no.nav.api.brukerRute.brukerRuter
import no.nav.api.gjennomforing.gjennomforingRuter
import no.nav.api.status.status
import no.nav.api.test.testDataRoutes

/**
 * Konfigurerer HTTP-ruter for applikasjonen.
 * @receiver Application Ktor-applikasjon.
 */
fun Application.configureRouting() {
    routing {
        brukerRuter()
        gjennomforingRuter()
        testDataRoutes()
        ukeAntallRecordsRoute()
        status()
    }
}
