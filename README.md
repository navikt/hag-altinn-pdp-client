# hag-altinn-pdp-client

Klient for å slå opp tilganger i altinn sitt PDP API

### Bakgrunnsinformasjon
For oversikt over altinn sitt pdp api se [altinn sin guide](https://docs.altinn.studio/nb/authorization/guides/resource-owner/integrating-link-service/#integrasjon-med-pdp) og [altinn sin dokumentasjon](https://docs.altinn.studio/nb/api/authorization/spec/#/Decision/post_authorize)

Altinn bruker xacml standarden for å definere policyer, ressurser, requester og responser for tilgangskontroll.
Denne klienten skjuler xacml detaljene for brukeren av klienten.

For å se nærmere på hvordan xacml fungerer se altinn sin [xacml guide](https://docs.altinn.studio/nb/authorization/reference/xacml/) og [xacml spesifikasjonen til oasis](https://docs.oasis-open.org/xacml/3.0/xacml-3.0-core-spec-os-en.html)

### Forutsetninger
- Maskinporten klient/integrasjon med tilgang til scope: `altinn:authorization/authorize`
- Veksle maskinporten token til altinnToken, se [altinn sin guide](https://docs.altinn.studio/nb/api/scenarios/authentication/#exchange-of-jwt-token)
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
// Sjekk at systembruker har tilgang til en gyldig ressurs på flere organisasjoner
val systembrukerResultat: Boolean = runBlocking { pdpClient.systemHarRettighetForOrganisasjoner("systembruker_id", setOf("gyldig_orgnr1", "gyldig_orgnr2"), "gyldigRessurs") } // true / false
// Sjekk at systembruker har tilgang til to gyldige ressurser på flere organisasjoner
val systembrukerFlereResurserResultat: Boolean = runBlocking { pdpClient.systemHarRettighetForOrganisasjonerForRessurser("systembruker_id", setOf("gyldig_orgnr1", "gyldig_orgnr2"), setOf("gyldigRessurs1", "gyldigRessurs2")) } // true / false
// Sjekk at person har tilgang til en gyldig ressurs på flere organisasjoner
val personbrukerResultat: Boolean = runBlocking { pdpClient.personHarRettighetForOrganisasjoner("fnr", setOf("gyldig_orgnr1", "gyldig_orgnr2"), "gyldigRessurs") }  // true / false
```
### Henvendelser

Spørsmål knyttet til koden kan rettes mot <helsearbeidsgiver@nav.no>.

### For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #helse-arbeidsgiver.
