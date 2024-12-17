# hag-altinn-pdp-client

Klient for å slå opp tilganger i altinn sitt PDP API

For oversikt over altinn sitt pdp api se [altinn sin guide](https://docs.altinn.studio/nb/authorization/guides/integrating-link-service/#integrasjon-med-pdp) og [altinn sin dokumentasjon](https://docs.altinn.studio/nb/api/authorization/spec/#/Decision/post_authorize)

### Forutsetninger
- Maskinporten klient/integrasjon med tilgang til scope: `altinn:authorization/authorize`
- Veksle maskinporten token til altinnToken, se [altinn sin guide](https://docs.altinn.studio/api/scenarios/authentication/#exchange-of-jwt-token)
- Gyldig SubscriptionKey fra altinn for å nå pdp endepunktet (må kontakte altinn for å få dette)
- Registrert ressurs i ressurs registeret til altinn

### Klienten kan brukes slik
```kt
// Funksjon for å hente en gyldig token fra altinn
fun getToken(): String = "gyldig token"

val pdpClient = PdpClient(
    baseUrl = "https://platform.tt02.altinn.no",
    subscriptionKey = "gyldig_subsriptionkey",
    ressurs = "nav_sykepenger_inntektsmelding-nedlasting",
    getToken = ::getToken
)
// Sjekk at systembruker har tilgang til ressursen nav_sykepenger_inntektsmelding-nedlasting på en organisasjon
val systembrukerResultat: Boolean = runBlocking { pdpClient.systemHarRettighetForOrganisasjon("systembruker_id", "gyldig_orgnr") } // true / false
// Sjekk at person har tilgang til ressursen nav_sykepenger_inntektsmelding-nedlasting på en organisasjon
val personbrukerResultat: Boolean = runBlocking { pdpClient.personHarRettighetForOrganisasjon("fnr", "gyldig_orgnr") }  // true / false
```
### Henvendelser

Spørsmål knyttet til koden kan rettes mot <helsearbeidsgiver@nav.no>.

### For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #helse-arbeidsgiver.
