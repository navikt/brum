package no.nav.api.test

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.uri
import io.ktor.server.routing.*
import io.ktor.server.response.*
import no.nav.data.getMiniCsv
import no.nav.data.getNoBehov
import no.nav.data.getRealTestData
import no.nav.data.getTestData1
import no.nav.data.getTestData2


fun Route.testDataRoutes() {
    get("/testData") {
        val dataset = call.request.queryParameters["dataset"]
        when (dataset) {
            "Real" -> call.respondText(getRealTestData(), contentType = ContentType.Text.CSV)
            "Mini.csv" -> call.respondText(getMiniCsv(), contentType = ContentType.Text.CSV)
            else -> call.respondText(
                "Ugyldig datasettnummer. Datasettet : $dataset. finnes ikke. Fullstendig forespørsel: ${call.request.uri}",
                status = HttpStatusCode.BadRequest
            )
        }
    }
}