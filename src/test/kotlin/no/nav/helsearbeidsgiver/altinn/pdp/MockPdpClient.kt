package no.nav.helsearbeidsgiver.altinn.pdp

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import no.nav.helsearbeidsgiver.utils.test.mock.mockStatic


fun mockPdpClient(status: HttpStatusCode, content: String = ""): PdpClient {
    val mockEngine = MockEngine {
        respond(
            content = content,
            status = status,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        )
    }

    val mockHttpClient = HttpClient(mockEngine) { customize() }

    return mockStatic(::createHttpClient) {
        every { createHttpClient() } returns mockHttpClient
        PdpClient("url", "key", { "" })
    }
}
