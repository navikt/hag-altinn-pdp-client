package no.nav.helsearbeidsgiver.altinn.pdp

fun lagPdpMultiRequest(
    bruker: Bruker,
    orgnrSet: Set<String>,
    ressurser: Set<String>,
) = PdpRequest(
    request =
        PdpRequest.XacmlJsonRequestExternal(
            returnPolicyIdList = true,
            accessSubject =
                listOf(
                    PdpRequest.XacmlJsonCategoryExternal(
                        id = "s1",
                        attribute =
                            listOf(
                                PdpRequest.XacmlJsonAttributeExternal(
                                    attributeId = bruker.attributeId,
                                    value = bruker.id,
                                ),
                            ),
                    ),
                ),
            action =
                listOf(
                    PdpRequest.XacmlJsonCategoryExternal(
                        id = "a1",
                        attribute =
                            listOf(
                                PdpRequest.XacmlJsonAttributeExternal(
                                    attributeId = "urn:oasis:names:tc:xacml:1.0:action:action-id",
                                    value = "access",
                                    dataType = "http://www.w3.org/2001/XMLSchema#string",
                                ),
                            ),
                    ),
                ),
            resource =
                orgnrSet.kombiner(ressurser).mapIndexed { i, (orgnr, ressurs) ->
                    PdpRequest.XacmlJsonCategoryExternal(
                        id = "r$i",
                        attribute =
                            listOf(
                                PdpRequest.XacmlJsonAttributeExternal(
                                    attributeId = "urn:altinn:resource",
                                    value = ressurs,
                                    includeInResult = true,
                                ),
                                PdpRequest.XacmlJsonAttributeExternal(
                                    attributeId = "urn:altinn:organization:identifier-no",
                                    value = orgnr,
                                    includeInResult = true,
                                ),
                            ),
                    )
                },
            multiRequests =
                PdpRequest.MultiRequestsExternal(
                    requestReference =
                        orgnrSet.kombiner(ressurser).mapIndexed { i, (orgnr, ressurs) ->
                            PdpRequest.RequestReferenceExternal(
                                referenceId = listOf("s1", "a1", "r$i"),
                            )
                        },
                ),
        ),
)

// Kombinerer to sett til en liste av par (kartesisk produkt)
private fun <T, R> Set<T>.kombiner(other: Set<R>): List<Pair<T, R>> =
    this.flatMap { first ->
        other.map { second ->
            first to second
        }
    }
