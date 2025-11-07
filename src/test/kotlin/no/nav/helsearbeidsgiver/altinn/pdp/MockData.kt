package no.nav.helsearbeidsgiver.altinn.pdp

import no.nav.helsearbeidsgiver.utils.json.jsonConfig

object MockData {
    val fnr = "01017012345"
    val orgnr = "312824450"
    val orgnumre = setOf(orgnr, "123456789")
    val systembrukerId = "1234"
    val person = Person(fnr)
    val system = System(systembrukerId)
    val imRessurs = "nav_sykepenger_inntektsmelding"
    val sykRessurs = "nav_sykepenger_sykmelding"
    val pdpPersonRequest = lagPdpMultiRequest(person, orgnumre, setOf(imRessurs))
    val pdpSystemRequest = lagPdpMultiRequest(system, orgnumre, setOf(imRessurs))
    val pdpSystemMultiRequest = lagPdpMultiRequest(system, orgnumre, setOf(imRessurs, sykRessurs))
    val permitResponseString: String =
        jsonConfig.encodeToString(
            PdpResponse.serializer(),
            PdpResponse(listOf(DecisionResult(Decision.Permit))),
        )
}

fun pdpResponseString(decisions: List<Decision>): String =
    jsonConfig.encodeToString(
        PdpResponse.serializer(),
        PdpResponse(decisions.map { DecisionResult(it) }),
    )
