package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
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

        "denied() returnerer tom liste når alle har Permit" {
            val response =
                PdpResponse(
                    listOf(
                        DecisionResult(
                            Decision.Permit,
                            category =
                                listOf(
                                    ResponseCategory(
                                        attribute =
                                            listOf(
                                                ResponseAttribute("urn:altinn:organization:identifier-no", "123456789"),
                                                ResponseAttribute("urn:altinn:resource", "nav_sykepenger_inntektsmelding"),
                                            ),
                                    ),
                                ),
                        ),
                    ),
                )
            response.denied().shouldBeEmpty()
        }

        "denied() returnerer nektet orgnr med ressurs og decision" {
            val response =
                PdpResponse(
                    listOf(
                        DecisionResult(
                            Decision.Permit,
                            category =
                                listOf(
                                    ResponseCategory(
                                        attribute =
                                            listOf(
                                                ResponseAttribute("urn:altinn:organization:identifier-no", "111111111"),
                                                ResponseAttribute("urn:altinn:resource", "nav_sykepenger_inntektsmelding"),
                                            ),
                                    ),
                                ),
                        ),
                        DecisionResult(
                            Decision.Deny,
                            category =
                                listOf(
                                    ResponseCategory(
                                        attribute =
                                            listOf(
                                                ResponseAttribute("urn:altinn:organization:identifier-no", "222222222"),
                                                ResponseAttribute("urn:altinn:resource", "nav_sykepenger_inntektsmelding"),
                                            ),
                                    ),
                                ),
                        ),
                        DecisionResult(
                            Decision.NotApplicable,
                            category =
                                listOf(
                                    ResponseCategory(
                                        attribute =
                                            listOf(
                                                ResponseAttribute("urn:altinn:organization:identifier-no", "333333333"),
                                                ResponseAttribute("urn:altinn:resource", "nav_sykepenger_sykmelding"),
                                            ),
                                    ),
                                ),
                        ),
                    ),
                )
            response.denied().shouldContainExactly(
                DeniedEntry("222222222", "nav_sykepenger_inntektsmelding", Decision.Deny),
                DeniedEntry("333333333", "nav_sykepenger_sykmelding", Decision.NotApplicable),
            )
        }

        "denied() fra multi-respons JSON inneholder riktige orgnr" {
            val validAltinnResponse = "pdp-response/multi-respons.json".readResource()
            val pdpResponse: PdpResponse =
                jsonConfig.decodeFromString(PdpResponse.serializer(), validAltinnResponse)
            pdpResponse.denied().shouldContainExactly(
                DeniedEntry("311910663", "nav_sykepenger_inntektsmelding", Decision.NotApplicable),
                DeniedEntry("311910663", "nav_sykepenger_sykmelding", Decision.NotApplicable),
            )
        }
    })
