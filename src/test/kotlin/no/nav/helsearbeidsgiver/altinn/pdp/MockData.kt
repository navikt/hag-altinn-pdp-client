package no.nav.helsearbeidsgiver.altinn.pdp

import no.nav.helsearbeidsgiver.utils.json.jsonConfig

object MockData {
    val fnr = "01017012345"
    val orgnr = "312824450"
    val orgnumre = setOf(orgnr, "123456789")
    val systembrukerId = "1234"
    val person = Person(fnr)
    val system = System(systembrukerId)
    val ressurs = "nav_sykepenger_inntektsmelding-nedlasting"
    val pdpPersonRequest = lagPdpRequest(person, setOf(orgnr), ressurs)
    val pdpSystemRequest = lagPdpRequest(system, orgnumre, ressurs)
    val permitResponseString: String =
        jsonConfig.encodeToString(
            PdpResponse.serializer(),
            PdpResponse(listOf(DecisionResult(Decision.Permit))),
        )
}
