package no.nav.helsearbeidsgiver.altinn.pdp

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PdpClient(
    private val baseUrl: String,
    private val subscriptionKey: String,
    private val ressurs: String,
    private val getToken: () -> String
) {
    private val httpClient = createHttpClient(3)

    suspend fun personHarRettighetForOrganisasjon(fnr: String, orgnr: String): Boolean =
        pdpKall(Person(fnr), orgnr).harTilgang()

    suspend fun systemHarRettighetForOrganisasjon(systembrukerId: String, orgnr: String): Boolean =
        pdpKall(System(systembrukerId), orgnr).harTilgang()

    private suspend fun pdpKall(bruker: Bruker, orgnr: String): PdpResponse {
        val pdpRequest = lagPdpRequest(bruker, orgnr, ressurs)
        val pdpResponse: PdpResponse = httpClient.post("$baseUrl/authorization/api/v1/authorize") {
            bearerAuth(getToken())
            header("Ocp-Apim-Subscription-Key", subscriptionKey)
            header("Content-Type", "application/json")
            header("Accept", "application/json")
            setBody(pdpRequest)
        }.body()
        return pdpResponse
    }
}
