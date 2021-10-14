package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType
import no.nav.pensjon.vtp.testmodell.personopplysning.GateadresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import java.util.*

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Person> {

    override fun get(env: DataFetchingEnvironment): Person? =
        with(personModellRepository.findById(env.getArgument("ident"))) {
            if (this == null) return@with null

            Person(
                navn = Navn(
                    fornavn = fornavn,
                    etternavn = etternavn,
                    metadata = Metadata.freg,
                ).asList(),
                foedsel = Foedsel(
                    foedselsdato = fødselsdato?.let { Date(it.toEpochDay()) },
                    metadata = Metadata.freg,
                ).asList(),
                doedsfall = Doedsfall(
                    doedsdato = dødsdato?.let { Date(it.toEpochDay()) },
                    metadata = Metadata.freg,
                ).asList(),
                statsborgerskap = statsborgerskap?.map { statsborgerskap ->
                    Statsborgerskap(
                        land = statsborgerskap.land.toString(),
                        gyldigFraOgMed = statsborgerskap.fom?.let { Date(it.toEpochDay()) },
                        gyldigTilOgMed = statsborgerskap.tom?.let { Date(it.toEpochDay()) },
                        metadata = Metadata.freg,
                    )
                } ?: emptyList(),
                bostedsadresse = adresser.mapBostedsadresser(),
            )
        }

}

private fun <T : Any> T.asList() = listOf(this)

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
