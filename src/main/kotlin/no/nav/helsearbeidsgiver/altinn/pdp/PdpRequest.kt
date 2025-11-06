package no.nav.helsearbeidsgiver.altinn.pdp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PdpRequest(
    val request: XacmlJsonRequestExternal,
) {
    @Serializable
    data class XacmlJsonRequestExternal(
        val returnPolicyIdList: Boolean,
        val accessSubject: List<XacmlJsonCategoryExternal>,
        val action: List<XacmlJsonCategoryExternal>,
        val resource: List<XacmlJsonCategoryExternal>,
        val multiRequests: MultiRequestsExternal? = null,
    )

    @Serializable
    data class XacmlJsonCategoryExternal(
        val id: String? = null,
        val attribute: List<XacmlJsonAttributeExternal>,
    )

    @Serializable
    data class XacmlJsonAttributeExternal(
        val attributeId: String,
        val value: String,
        val dataType: String? = null,
    )

    @Serializable
    data class MultiRequestsExternal(
        val requestReference: List<RequestReferenceExternal>,
    )

    @Serializable
    data class RequestReferenceExternal(
        val referenceId: List<String>,
    )

    override fun toString(): String = Json.encodeToString(this)
}

sealed class Bruker(
    val id: String,
    val attributeId: String,
)

class Person(
    id: String,
) : Bruker(id, "urn:altinn:person:identifier-no")

class System(
    id: String,
) : Bruker(id, "urn:altinn:systemuser:uuid")
