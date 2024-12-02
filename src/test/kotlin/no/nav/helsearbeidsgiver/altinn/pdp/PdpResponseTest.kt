package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import no.nav.helsearbeidsgiver.utils.json.jsonConfig
import no.nav.helsearbeidsgiver.utils.test.resource.readResource
import io.kotest.data.row


class PdpResponseTest : StringSpec({
"Deserialiser PDP respons til objekt" {

    listOf(
        row("permit", Decision.Permit),
        row("not-applicable", Decision.NotApplicable),
        row("indeterminate", Decision.Indeterminate),
        row("deny", Decision.Deny),
    ).forEach{(response, decision) ->
        withClue("pdp response $response resulterer i Enum verdi ${Decision.Permit}") {
            val validAltinnResponse = "pdp-response/${response}.json".readResource()
            val pdpResponse: PdpResponse = jsonConfig.decodeFromString(PdpResponse.serializer(), validAltinnResponse)
            pdpResponse.response.first().decision shouldBe decision
        }
    }
    }
})
