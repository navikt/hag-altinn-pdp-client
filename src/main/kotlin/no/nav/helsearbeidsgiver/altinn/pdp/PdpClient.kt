package no.nav.helsearbeidsgiver.altinn.pdp

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
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
    ): Boolean = pdpKallMulti(Person(fnr), orgnumre, setOf(ressurs)).getOrThrow().harTilgang()

    suspend fun systemHarRettighetForOrganisasjoner(
        systembrukerId: String,
        orgnumre: Set<String>,
        ressurs: String,
    ): Boolean = pdpKallMulti(System(systembrukerId), orgnumre, setOf(ressurs)).getOrThrow().harTilgang()

    suspend fun systemHarRettighetForOrganisasjonerForRessurser(
        systembrukerId: String,
        orgnumre: Set<String>,
        ressurser: Set<String>,
    ): Boolean = pdpKallMulti(System(systembrukerId), orgnumre, ressurser).getOrThrow().harTilgang()

    private suspend fun pdpKallMulti(
        bruker: Bruker,
        orgnumre: Set<String>,
        ressurser: Set<String>,
    ): Result<PdpResponse> {
        if (orgnumre.isEmpty()) {
            "Ingen organisasjonsnumre gitt for pdp-kall".also {
                logger.warn(it)
                sikkerLogger.warn(it)
                throw IllegalArgumentException(it)
            }
        }
        if (ressurser.isEmpty()) {
            "Ingen ressurser gitt for pdp-kall".also {
                logger.warn(it)
                sikkerLogger.warn(it)
                throw IllegalArgumentException(it)
            }
        }
        val pdpRequest = lagPdpMultiRequest(bruker, orgnumre, ressurser)
        sikkerLogger.debug("PDP kall for $ressurser: $pdpRequest")
        val pdpResponseResult: Result<PdpResponse> =
            runCatching<PdpClient, PdpResponse> {
                httpClient
                    .post("$baseUrl/authorization/api/v1/authorize") {
                        header("Ocp-Apim-Subscription-Key", subscriptionKey)
                        header("Content-Type", "application/json")
                        header("Accept", "application/json")
                        setBody(pdpRequest)
                    }.let { response ->
                        val raw = response.bodyAsText()
                        sikkerLogger.debug("Raw PDP respons: $raw")
                        response.body<PdpResponse>()
                    }
            }.recover { e ->
                "Feil ved kall til pdp endepunkt".also {
                    logger.error(it)
                    sikkerLogger.error(it, e)
                }
                throw PdpClientException()
            }
        sikkerLogger.debug("PDP respons: $pdpResponseResult")
        pdpResponseResult.getOrNull()?.also {
            logger.debug("PDP respons har tilgang: ${it.harTilgang()}")
        }
        return pdpResponseResult
    }
}

class PdpClientException :
    Exception(
        "Feil ved kall til pdp endepunkt",
    )
