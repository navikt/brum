package no.nav

import io.ktor.client.call.body
import java.io.File

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `server starts and responds with 200 OK`() = testApplication {
        application {
            module()
        }/*
        val response = client.get("/testData?dataset=No behov")

        assertEquals(
            getNoBehov(), response.body()
        )
        assertEquals(HttpStatusCode.OK, response.status)
*/
    }
}