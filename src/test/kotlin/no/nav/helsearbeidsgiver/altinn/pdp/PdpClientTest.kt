package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class PdpClientTest :
    FunSpec({
        test("Pdp oppslag for personbruker gir Permit") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnumre, MockData.ressurs) shouldBe true
        }

        test("Håndterer hvis personbruker-kallet timer ut") {
            val pdpClient = mockPdpClient(HttpStatusCode.GatewayTimeout)
            shouldThrowExactly<PdpClientException> {
                pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnumre, MockData.ressurs)
            }
        }

        test("Kaster feil ved Unauthorized for personbruker") {
            val pdpClient = mockPdpClient(HttpStatusCode.Unauthorized)
            shouldThrowExactly<PdpClientException> {
                pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnumre, MockData.ressurs)
            }
        }

        test("Kaster feil dersom man ikke gir organisasjonsnumre for personbruker") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            shouldThrowExactly<IllegalArgumentException> {
                pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, emptySet(), MockData.ressurs)
            }
        }

        test("Pdp oppslag for systembruker gir Permit") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            pdpClient.systemHarRettighetForOrganisasjoner(
                MockData.systembrukerId,
                MockData.orgnumre,
                MockData.ressurs,
            ) shouldBe true
        }

        test("Håndterer hvis systembruker-kallet timer ut") {
            val pdpClient = mockPdpClient(HttpStatusCode.GatewayTimeout)
            shouldThrowExactly<PdpClientException> {
                pdpClient.systemHarRettighetForOrganisasjoner(
                    MockData.systembrukerId,
                    MockData.orgnumre,
                    MockData.ressurs,
                )
            }
        }

        test("Kaster feil ved Unauthorized for systembruker") {
            val pdpClient = mockPdpClient(HttpStatusCode.Unauthorized)
            shouldThrowExactly<PdpClientException> {
                pdpClient.systemHarRettighetForOrganisasjoner(
                    MockData.systembrukerId,
                    MockData.orgnumre,
                    MockData.ressurs,
                )
            }
        }

        test("Kaster feil dersom man ikke gir organisasjonsnumre for systembruker") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            shouldThrowExactly<IllegalArgumentException> {
                pdpClient.systemHarRettighetForOrganisasjoner(
                    MockData.systembrukerId,
                    emptySet(),
                    MockData.ressurs,
                )
            }
        }
    })
