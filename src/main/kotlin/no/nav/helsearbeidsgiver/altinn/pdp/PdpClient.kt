package no.nav.helsearbeidsgiver.altinn.pdp

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import no.nav.helsearbeidsgiver.utils.log.logger
import no.nav.helsearbeidsgiver.utils.log.sikkerLogger

class PdpClient(
    private val baseUrl: String,
    private val subscriptionKey: String,
    private val getToken: () -> String,
) {
    private val httpClient = createHttpClient(3, getToken)

    private val logger = this.logger()
    private val sikkerLogger = sikkerLogger()

    suspend fun personHarRettighetForOrganisasjoner(
        fnr: String,
        orgnumre: Set<String>,
        ressurs: String,
    ): Boolean = pdpKall(Person(fnr), orgnumre, ressurs).getOrThrow().harTilgang()

    suspend fun systemHarRettighetForOrganisasjoner(
        systembrukerId: String,
        orgnumre: Set<String>,
        ressurs: String,
    ): Boolean = pdpKall(System(systembrukerId), orgnumre, ressurs).getOrThrow().harTilgang()

    private suspend fun pdpKall(
        bruker: Bruker,
        orgnumre: Set<String>,
        ressurs: String,
    ): Result<PdpResponse> {
        if (orgnumre.isEmpty()) {
            "Ingen organisasjonsnumre gitt for pdp-kall".also {
                logger.warn(it)
                sikkerLogger.warn(it)
                throw IllegalArgumentException(it)
            }
        }
        val pdpRequest = lagPdpRequest(bruker, orgnumre, ressurs)
        sikkerLogger.info("PDP kall for $ressurs: $pdpRequest")
        val pdpResponseResult: Result<PdpResponse> =
            runCatching<PdpClient, PdpResponse> {
                httpClient
                    .post("$baseUrl/authorization/api/v1/authorize") {
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
        sikkerLogger.info("PDP respons: $pdpResponseResult")
        return pdpResponseResult
    }
}

class PdpClientException :
    Exception(
        "Feil ved kall til pdp endepunkt",
    )
