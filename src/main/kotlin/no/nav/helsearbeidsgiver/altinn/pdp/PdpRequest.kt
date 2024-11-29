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
