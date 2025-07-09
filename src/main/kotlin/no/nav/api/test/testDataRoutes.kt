package no.nav.api.test

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.uri
import io.ktor.server.routing.*
import io.ktor.server.response.*
import no.nav.data.getNoBehov
import no.nav.data.getRealTestData
import no.nav.data.getTestData1
import no.nav.data.getTestData2


fun Route.testDataRoutes() {
    get("/testData") {
        val dataset = call.request.queryParameters["dataset"]
        when (dataset) {
            "Test1" -> call.respond(getTestData1())
            "Test2" -> call.respond(getTestData2())
            "No behov" -> call.respond(getNoBehov())
            "Real" -> call.respondText(getRealTestData())
            else -> call.respondText(
                "Ugyldig datasettnummer. Datasettet : $dataset. finnes ikke. Fullstendig forespørsel: ${call.request.uri}",
                status = HttpStatusCode.BadRequest
            )
        }
    }
}