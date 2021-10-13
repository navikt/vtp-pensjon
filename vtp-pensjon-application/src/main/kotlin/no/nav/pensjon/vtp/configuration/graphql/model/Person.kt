package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType
import no.nav.pensjon.vtp.testmodell.personopplysning.GateadresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import java.util.*

data class Person(
    val navn: List<Navn> = emptyList(),
    val foedsel: List<Foedsel> = emptyList(),
    val doedsfall: List<Doedsfall> = emptyList(),
    val statsborgerskap: List<Statsborgerskap> = emptyList(),
    val bostedsadresse: List<Bostedsadresse> = emptyList(),
    val kontaktadresse: List<Kontaktadresse> = emptyList(),
    val oppholdsadresse: List<Oppholdsadresse> = emptyList()
)

fun <T : Any> T.asList() = listOf(this)

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Person> {
    override fun get(env: DataFetchingEnvironment) =
        with(personModellRepository.findById(env.getArgument("ident"))) {
            if (this == null) return@with null

            Person(
                navn = Navn(
                    fornavn = fornavn,
                    mellomnavn = null,
                    etternavn = etternavn,
                    forkortetNavn = null,
                    gyldigFraOgMed = null,
                    metadata = Metadata.freg,
                    folkeregistermetadata = null,
                ).asList(),
                foedsel = Foedsel(
                    foedselsaar = null,
                    foedselsdato = fødselsdato?.let { Date(it.toEpochDay()) },
                    foedeland = null,
                    foedested = null,
                    foedekommune = null,
                    metadata = Metadata.freg,
                ).asList(),
                doedsfall = Doedsfall(
                    doedsdato = dødsdato?.let { Date(it.toEpochDay()) },
                    metadata = Metadata.freg,
                ).asList(),
                statsborgerskap = statsborgerskap?.map { statsborgerskap ->
                    Statsborgerskap(
                        land = statsborgerskap.land.toString(),
                        bekreftelsesdato = null,
                        gyldigFraOgMed = statsborgerskap.fom?.let { d -> Date(d.toEpochDay()) },
                        gyldigTilOgMed = statsborgerskap.tom?.let { d -> Date(d.toEpochDay()) },
                        metadata = Metadata.freg,
                    )
                } ?: emptyList(),
                bostedsadresse = adresser.mapBostedsadresser(),
            )
        }
}

fun List<AdresseModell>.mapBostedsadresser() =
    filter { it.adresseType == AdresseType.BOSTEDSADRESSE }.mapNotNull {
        when (it) {
            is GateadresseModell -> Bostedsadresse(
                gyldigFraOgMed = it.fom?.atStartOfDay(),
                gyldigTilOgMed = it.tom?.atStartOfDay(),
                vegadresse = Vegadresse(
                    adressenavn = it.gatenavn,
                    husnummer = it.husnummer?.toString(),
                    husbokstav = it.husbokstav,
                    postnummer = it.postnummer
                ),
                metadata = Metadata.freg,
            )
            else -> null
        }
    }
