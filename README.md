# hag-altinn-pdp-client

Klient for å slå opp tilganger i altinn sitt PDP API

For oversikt over altinn sitt pdp api se [altinn sin guide](https://docs.altinn.studio/nb/authorization/guides/integrating-link-service/#integrasjon-med-pdp) og [altinn sin dokumentasjon](https://docs.altinn.studio/nb/api/authorization/spec/#/Decision/post_authorize)

### Forutsetninger
- Maskinporten klient/integrasjon med tilgang til scope: `altinn:authorization/authorize`
- Veksle maskinporten token til altinnToken, se [altinn sin guide](https://docs.altinn.studio/api/scenarios/authentication/#exchange-of-jwt-token)
- Gyldig SubscriptionKey fra altinn for å nå pdp endepunktet (må kontakte altinn for å få dette)
- Registrert ressurs i ressurs registeret til altinn

### Publisere nye versjoner

For å publisere snapshots, push til en branch som starter med `dev/`.
Snapshot-versjonen er basert på `version` i `gradle.properties`. Ved `version=1.2.3` så vil workflow publisere en snapshot `1.2.3-SNAPSHOT`.
Snapshot-versjoner overskrives for hvert push.

For å publisere ny versjon, oppdater `version` i `gradle.properties` og push til branch `main`.
Dersom versjon allerede eksisterer så vil workflow feile med `409 Conflict`.

### Klienten kan brukes slik
```kt
// Funksjon for å hente en gyldig token fra altinn
fun getToken(): String = "gyldig token"

val pdpClient = PdpClient(
    baseUrl = "https://platform.tt02.altinn.no",
    subscriptionKey = "gyldig_subsriptionkey",
    getToken = ::getToken
)
// Sjekk at systembruker har tilgang til en gyldig ressurs på en organisasjon
val systembrukerResultat: Boolean = runBlocking { pdpClient.systemHarRettighetForOrganisasjoner("systembruker_id", setOf("gyldig_orgnr1", "gyldig_orgnrN"), "gyldigRessurs") } // true / false
// Sjekk at person har tilgang til en gyldig ressurs på en organisasjon
val personbrukerResultat: Boolean = runBlocking { pdpClient.personHarRettighetForOrganisasjoner("fnr", setOf("gyldig_orgnr1", "gyldig_orgnrN"), "gyldigRessurs") }  // true / false
```
### Henvendelser

Spørsmål knyttet til koden kan rettes mot <helsearbeidsgiver@nav.no>.

### For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #helse-arbeidsgiver.
