package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import no.nav.helsearbeidsgiver.utils.json.jsonConfig

class PdpRequestTest : FunSpec({

    val requestBody = lagPdpRequest("01017012345", "312824450","nav_sykepenger_inntektsmelding-nedlasting")
    test("Serialialiser request til string") {
        val requestString = jsonConfig.encodeToString(PdpRequest.serializer(), requestBody)
        requestString shouldContain "01017012345"
        requestString shouldContain "nav_sykepenger_inntektsmelding-nedlasting"
        requestString shouldContain "312824450"
    }
})
