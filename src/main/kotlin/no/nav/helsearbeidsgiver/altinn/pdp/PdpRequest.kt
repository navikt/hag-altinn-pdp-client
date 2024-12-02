package no.nav.helsearbeidsgiver.altinn.pdp

import kotlinx.serialization.Serializable

@Serializable
data class PdpRequest(
    val request: XacmlJsonRequestExternal
) {
    @Serializable
    data class XacmlJsonRequestExternal(
        val returnPolicyIdList: Boolean,
        val accessSubject: List<XacmlJsonCategoryExternal>,
        val action: List<XacmlJsonCategoryExternal>,
        val resource: List<XacmlJsonCategoryExternal>
    )

    @Serializable
    data class XacmlJsonCategoryExternal(
        val attribute: List<XacmlJsonAttributeExternal>
    )

    @Serializable
    data class XacmlJsonAttributeExternal(
        val attributeId: String,
        val value: String,
        val dataType: String? = null
    )
}
//TODO ha en egen construktør for dette
fun lagPdpRequest(fnr: String, orgnr: String, ressurs: String) =
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
                                        value = fnr
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
                                        value = ressurs
                                    ),
                                    PdpRequest.XacmlJsonAttributeExternal(
                                        attributeId = "urn:altinn:organization:identifier-no",
                                        value = orgnr
                                    )
                                )
                        )
                    )
            )
    )

