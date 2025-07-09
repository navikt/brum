//package no.nav.api.ukeAntall
//
//import io.ktor.http.HttpStatusCode
//import io.ktor.server.auth.authenticate
//import io.ktor.server.request.uri
//import io.ktor.server.response.respond
//import io.ktor.server.routing.Route
//import no.nav.service.GjennomforingService
//import io.ktor.server.routing.*
//import no.nav.logger
//import no.nav.models.FeilRespons
//import no.nav.service.MockService
//
//fun Route.ukeAntall(){
//    authenticate("auth-bearer"){
//        val service = MockService()
//
//        get("/ukeAntall") {
//            val arstall = call.request.queryParameters["år"]
//            val ukeNr = call.request.queryParameters["uke"]
//           try {
//               val json = MockService.hentMockData("brum-dev-b72f", arstall, ukeNr)
//               call.respond(HttpStatusCode.OK, json)
//           } catch (e: Exception){
//               logger.error("Feil ved /ukeAntall ${call.request.uri}", e)
//               call.respond(HttpStatusCode.InternalServerError, FeilRespons("Feil ved henting av ukeAntallData: ${e.message}"))
//           }
//
//        }
//    }
//}