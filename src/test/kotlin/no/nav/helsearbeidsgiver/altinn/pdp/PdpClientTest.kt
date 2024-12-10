package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class PdpClientTest :
    FunSpec({
        test("Pdp oppslag gir Permit") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnr) shouldBe true
        }

        test("Håndterer hvis kallet timer ut") {
            val pdpClient = mockPdpClient(HttpStatusCode.GatewayTimeout)
            shouldThrowExactly<PdpClientException> {
                pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnr)
            }
        }

        test("Pdp oppslag for personbruker gir Permit") {
            val pdpClient = mockPdpClient(HttpStatusCode.Created, MockData.permitResponseString)
            pdpClient.systemHarRettighetForOrganisasjon(MockData.systembrukerId, MockData.orgnr) shouldBe true
        }

        test("Kaster feil ved Unauthorized") {
            val pdpClient = mockPdpClient(HttpStatusCode.Unauthorized)
            shouldThrowExactly<PdpClientException> {
                pdpClient.personHarRettighetForOrganisasjon(MockData.fnr, MockData.orgnr)
            }
        }
    })
