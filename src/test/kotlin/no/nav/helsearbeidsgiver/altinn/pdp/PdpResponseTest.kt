package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import no.nav.helsearbeidsgiver.utils.json.jsonConfig
import no.nav.helsearbeidsgiver.utils.test.resource.readResource

class PdpResponseTest :
    StringSpec({
        "Deserialiser PDP respons til objekt med riktig enum type" {
            listOf(
                Triple("permit", listOf(Decision.Permit), true),
                Triple("not-applicable", listOf(Decision.NotApplicable), false),
                Triple("indeterminate", listOf(Decision.Indeterminate), false),
                Triple("deny", listOf(Decision.Deny), false),
                Triple(
                    "multi-respons",
                    listOf(Decision.Permit, Decision.Permit, Decision.NotApplicable, Decision.NotApplicable),
                    false,
                ),
                Triple("empty", emptyList(), false),
            ).forEach { (responseType, resultat, tilgang) ->
                withClue("pdp response $responseType resulterer i Enum verdi ${Decision.Permit}") {
                    val validAltinnResponse = "pdp-response/$responseType.json".readResource()
                    val pdpResponse: PdpResponse =
                        jsonConfig.decodeFromString(PdpResponse.serializer(), validAltinnResponse)
                    pdpResponse.resultat() shouldBe resultat
                    pdpResponse.harTilgang() shouldBe tilgang
                }
            }
        }
    })
