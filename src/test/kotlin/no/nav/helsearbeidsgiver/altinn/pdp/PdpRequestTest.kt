package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.json.Json

class PdpRequestTest : FunSpec({

    val requestBody =
        PdpRequest(
            request =
            PdpRequest.XacmlJsonRequestExternal(
                returnPolicyIdList = true,
                accessSubject =
                listOf(
                    PdpRequest.XacmlJsonCategoryExternal(
                        attribute =
                        listOf(
                            PdpRequest.XacmlJsonAttributeExternal(
                                attributeId = "urn:altinn:person:identifier-no",
                                value = "01017012345"
                            )
                        )
                    )
                ),
                action =
                listOf(
                    PdpRequest.XacmlJsonCategoryExternal(
                        attribute =
                        listOf(
                            PdpRequest.XacmlJsonAttributeExternal(
                                attributeId = "urn:oasis:names:tc:xacml:1.0:action:action-id",
                                value = "write",
                                dataType = "http://www.w3.org/2001/XMLSchema#string"
                            )
                        )
                    )
                ),
                resource =
                listOf(
                    PdpRequest.XacmlJsonCategoryExternal(
                        attribute =
                        listOf(
                            PdpRequest.XacmlJsonAttributeExternal(
                                attributeId = "urn:altinn:resource",
                                value = "nav_sykepenger_inntektsmelding-nedlasting"
                            ),
                            PdpRequest.XacmlJsonAttributeExternal(
                                attributeId = "urn:altinn:organization:identifier-no",
                                value = "312824450"
                            )
                        )
                    )
                )
            )
        )
    test("Serialialiser request til string") {
        val requestString = Json.encodeToString(PdpRequest.serializer(), requestBody)
        requestString shouldContain "01017012345"
        requestString shouldContain "nav_sykepenger_inntektsmelding-nedlasting"
        requestString shouldContain "312824450"
    }
})
