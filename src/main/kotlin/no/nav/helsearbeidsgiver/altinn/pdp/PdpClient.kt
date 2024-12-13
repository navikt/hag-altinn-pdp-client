package no.nav.helsearbeidsgiver.altinn.pdp

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import no.nav.helsearbeidsgiver.utils.log.logger
import no.nav.helsearbeidsgiver.utils.log.sikkerLogger

class PdpClient(
    private val baseUrl: String,
    private val subscriptionKey: String,
    private val ressurs: String,
    private val getToken: () -> String,
) {
    private val httpClient = createHttpClient(3)

    private val logger = this.logger()
    private val sikkerLogger = sikkerLogger()

    suspend fun personHarRettighetForOrganisasjon(
        fnr: String,
        orgnr: String,
    ): Boolean = pdpKall(Person(fnr), orgnr).getOrThrow().harTilgang()

    suspend fun systemHarRettighetForOrganisasjon(
        systembrukerId: String,
        orgnr: String,
    ): Boolean = pdpKall(System(systembrukerId), orgnr).getOrThrow().harTilgang()

    private suspend fun pdpKall(
        bruker: Bruker,
        orgnr: String,
    ): Result<PdpResponse> {
        val pdpRequest = lagPdpRequest(bruker, orgnr, ressurs)
        val pdpResponseResult: Result<PdpResponse> =
            runCatching<PdpClient, PdpResponse> {
                httpClient
                    .post("$baseUrl/authorization/api/v1/authorize") {
                        bearerAuth(getToken())
                        header("Ocp-Apim-Subscription-Key", subscriptionKey)
                        header("Content-Type", "application/json")
                        header("Accept", "application/json")
                        setBody(pdpRequest)
                    }.body()
            }.recover { e ->
                "Feil ved kall til pdp endepunkt".also {
                    logger.error(it)
                    sikkerLogger.error(it, e)
                }
                throw PdpClientException()
            }
        return pdpResponseResult
    }
}

class PdpClientException() :
    Exception(
        "Feil ved kall til pdp endepunkt",
    )
