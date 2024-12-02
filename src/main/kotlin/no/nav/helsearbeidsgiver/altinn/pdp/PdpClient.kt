package no.nav.helsearbeidsgiver.altinn.pdp

class PdpClient(
    private val url: String,
    private val subscriptionKey: String,
    private val getToken: () -> String
){
    private val httpClient = createHttpClient()
    suspend fun harRettighetForOrganisasjon(fnr: String, orgnr: String) : Boolean = false

}
