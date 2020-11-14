package no.nav.pensjon.vtp.testmodell.organisasjon

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class OrganisasjonModell(
        @Id
        val orgnummer: String,
        val navn: Navn?,
        val organisasjonDetaljer: OrganisasjonDetaljerModell?
)
