package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository

data class Person(
    val navn: Navn,
    val foedsel: Foedsel
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
                foedselsdato = null,
                foedeland = null,
                foedested = null,
                foedekommune = null,
                metadata = Metadata(false, "FREG")
            )
        )
    }
}
