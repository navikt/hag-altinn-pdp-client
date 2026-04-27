package no.nav.helsearbeidsgiver.altinn.pdp

import kotlinx.serialization.Serializable

@Serializable
data class PdpResponse(
    val response: List<DecisionResult>,
)

@Serializable
data class DecisionResult(
    val decision: Decision,
    val category: List<ResponseCategory>? = null,
)

@Serializable
data class ResponseCategory(
    val attribute: List<ResponseAttribute>? = null,
)

@Serializable
data class ResponseAttribute(
    val attributeId: String,
    val value: String,
)

enum class Decision {
    Permit,
    Indeterminate,
    NotApplicable,
    Deny,
}

fun PdpResponse.resultat() = response.map { it.decision }

fun PdpResponse.harTilgang(): Boolean = response.isNotEmpty() && resultat().all { it == Decision.Permit }

fun PdpResponse.denied(): List<DeniedEntry> =
    response
        .filter { it.decision != Decision.Permit }
        .mapNotNull { result ->
            val attributes = result.category?.flatMap { it.attribute.orEmpty() }.orEmpty()
            val orgnr = attributes.find { it.attributeId == "urn:altinn:organization:identifier-no" }?.value
            val ressurs = attributes.find { it.attributeId == "urn:altinn:resource" }?.value
            if (orgnr != null) {
                DeniedEntry(orgnr = orgnr, ressurs = ressurs, decision = result.decision)
            } else {
                null
            }
        }

data class DeniedEntry(
    val orgnr: String,
    val ressurs: String?,
    val decision: Decision,
)
