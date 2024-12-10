package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.nav.helsearbeidsgiver.utils.json.jsonConfig
import no.nav.helsearbeidsgiver.utils.test.resource.readResource

class PdpResponseTest :
    StringSpec({
        "Deserialiser PDP respons til objekt med riktig enum type" {
            listOf(
                row("permit", Decision.Permit),
                row("not-applicable", Decision.NotApplicable),
                row("indeterminate", Decision.Indeterminate),
                row("deny", Decision.Deny),
            ).forEach { (responseType, decision) ->
                withClue("pdp response $responseType resulterer i Enum verdi ${Decision.Permit}") {
                    val validAltinnResponse = "pdp-response/$responseType.json".readResource()
                    val pdpResponse: PdpResponse = jsonConfig.decodeFromString(PdpResponse.serializer(), validAltinnResponse)
                    pdpResponse.resultat() shouldBe decision
                }
            }
        }
    })
