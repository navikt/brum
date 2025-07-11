package no.nav.api.test

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.uri
import io.ktor.server.routing.*
import io.ktor.server.response.*
import no.nav.data.getMiniCsv
import no.nav.data.getRealTestData

fun Route.testDataRoutes() {
    get("/testData") {
        val dataset = call.request.queryParameters["dataset"]
        when (dataset) {
            "Real" -> call.respondText(getRealTestData(), contentType = ContentType.Text.CSV)
            "Mini" -> call.respond(getMiniCsv())
            else -> call.respondText(
                "Ugyldig datasettnummer. Datasettet : $dataset. finnes ikke. Fullstendig forespørsel: ${call.request.uri}",
                status = HttpStatusCode.BadRequest
            )
        }
    }
}