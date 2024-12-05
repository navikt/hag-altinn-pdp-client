package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import no.nav.helsearbeidsgiver.utils.json.jsonConfig

class PdpRequestTest : FunSpec({
    test("Serialialiser request for person riktig") {
        val requestString = jsonConfig.encodeToString(PdpRequest.serializer(), Mock.pdpPersonRequest)
        requestString shouldContain Mock.fnr
        requestString shouldContain "nav_sykepenger_inntektsmelding-nedlasting"
        requestString shouldContain "urn:altinn:person:identifier-no"
        requestString shouldContain Mock.orgnr
    }

    test("Serialialiser request for systembruker riktig") {
        val requestString = jsonConfig.encodeToString(PdpRequest.serializer(), Mock.pdpSystemRequest)
        requestString shouldContain Mock.systembrukerId
        requestString shouldContain "nav_sykepenger_inntektsmelding-nedlasting"
        requestString shouldContain "urn:altinn:systemuser:uuid"
        requestString shouldContain Mock.orgnr
    }
})
