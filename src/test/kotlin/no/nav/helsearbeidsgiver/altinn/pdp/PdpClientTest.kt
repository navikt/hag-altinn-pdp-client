package no.nav.helsearbeidsgiver.altinn.pdp

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class PdpClientTest : FunSpec({
    val pdpClient = mockPdpClient(HttpStatusCode.Created, "{}")
    val requestBody = lagPdpRequest("01017012345", "312824450","nav_sykepenger_inntektsmelding-nedlasting")
    test("Pdp oppslag gir Permit") {
        pdpClient.harRettighetForOrganisasjon("01017012345","312824450" ) shouldBe true
    }
})

object Mock{
    val fnr = "01017012345"
}
