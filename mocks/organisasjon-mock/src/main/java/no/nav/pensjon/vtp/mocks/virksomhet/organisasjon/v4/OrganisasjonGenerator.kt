package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v4

import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Enhetstyper
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.ObjectFactory
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.UstrukturertNavn
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentNoekkelinfoOrganisasjonResponse

private data class MockOrganisasjon(
    val navnelinjer: List<String>,
    val enhetstype: String
)

const val eplehusetNorgeAS = "EPLEHUSET NORGE AS"
const val sesamNorgeAS = "SESAM AS"

private val mockOrganisasjons: Map<String, MockOrganisasjon> =
    mapOf(
        Pair(
            "986498192",
            MockOrganisasjon(
                navnelinjer = listOf(eplehusetNorgeAS),
                enhetstype = "AS"
            )
        ),
        Pair(
            "986507035",
            MockOrganisasjon(
                navnelinjer = listOf(eplehusetNorgeAS, "AVD SOLSIDEN"),
                enhetstype = "BEDR"
            )
        ),
        Pair(
            "976030788",
            MockOrganisasjon(
                navnelinjer = listOf(sesamNorgeAS),
                enhetstype = "AS"
            )
        ),
        Pair(
            "976037286",
            MockOrganisasjon(
                navnelinjer = listOf(sesamNorgeAS),
                enhetstype = "BEDR"
            )
        )
    )

private val defaultOrganisation = MockOrganisasjon(
    navnelinjer = listOf("IKKE NOEN AS", "AVD MOCK"),
    enhetstype = "BEDR"
)

/**
 * Genererer tilfeldige organisasjoner til testformål. Første utgave gir et enkelt navn og enhetstype til innkommende orgnr. Vi ikke feile.
 */
private val objectFactory = ObjectFactory()

fun lagHentNoekkelinfoOrganisasjonResponse(orgnummer: String): HentNoekkelinfoOrganisasjonResponse {
    val mockOrganisasjon = mockOrganisasjons.getOrDefault(orgnummer, defaultOrganisation)

    return HentNoekkelinfoOrganisasjonResponse().apply {
        this.orgnummer = orgnummer
        navn = lagUstrukturertNavn(mockOrganisasjon)
        enhetstype = lagEnhetstype(mockOrganisasjon)
    }
}

private fun lagUstrukturertNavn(mockOrganisasjon: MockOrganisasjon): UstrukturertNavn {
    return objectFactory.createUstrukturertNavn().apply {
        navnelinje += mockOrganisasjon.navnelinjer
    }
}

private fun lagEnhetstype(mockOrganisasjon: MockOrganisasjon): Enhetstyper {
    return objectFactory.createEnhetstyper().apply {
        kodeRef = mockOrganisasjon.enhetstype
    }
}
