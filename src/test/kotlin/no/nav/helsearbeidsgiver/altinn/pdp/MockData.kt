package no.nav.helsearbeidsgiver.altinn.pdp

import no.nav.helsearbeidsgiver.utils.json.jsonConfig

object Mock {
    val fnr = "01017012345"
    val orgnr = "312824450"
    val systembrukerId = "1234"
    val person = Person(fnr)
    val system = System(systembrukerId)
    val ressurs = "nav_sykepenger_inntektsmelding-nedlasting"
    val pdpPersonRequest = lagPdpRequest(person, orgnr, ressurs)
    val pdpSystemRequest = lagPdpRequest(system, orgnr, ressurs)
    val permitResponseString: String =
        jsonConfig.encodeToString(
            PdpResponse.serializer(),
            PdpResponse(listOf(DecisionResult(Decision.Permit))),
        )
}
