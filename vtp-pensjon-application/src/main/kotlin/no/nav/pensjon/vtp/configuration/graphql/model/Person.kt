package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import java.util.*

data class Person(
    val navn: Navn,
    val foedsel: Foedsel,
    val doedsfall: Doedsfall,
    val statsborgerskap: Statsborgerskap,
)

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Person> {
    override fun get(env: DataFetchingEnvironment) = personModellRepository.findById(env.getArgument("ident"))?.let {
        Person(
            Navn(
                fornavn = it.fornavn,
                mellomnavn = null,
                etternavn = it.etternavn,
                forkortetNavn = null,
                gyldigFraOgMed = null,
                metadata = Metadata(false, "FREG")
            ),
            Foedsel(
                foedselsaar = null,
                foedselsdato = it.fødselsdato?.let { it_d -> Date(it_d.toEpochDay()) },
                foedeland = null,
                foedested = null,
                foedekommune = null,
                metadata = Metadata(false, "FREG")
            ),
            Doedsfall(
                doedsdato = it.dødsdato?.let { it_d -> Date(it_d.toEpochDay()) },
                metadata = Metadata(false, "FREG")
            ),
            Statsborgerskap(
                land = it.statsborgerskap!!.firstOrNull()?.land.toString(),
                bekreftelsesdato = null,
                gyldigFraOgMed = Date(it.statsborgerskap.first().fom!!.toEpochDay()),
                gyldigTilOgMed = Date(it.statsborgerskap.first().tom!!.toEpochDay()),
                metadata = Metadata(false, "FREG")
            )
        )
    }
}
