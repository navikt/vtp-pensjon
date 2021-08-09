package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository

data class Person(
    val navn: Navn
)

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Person> {
    override fun get(env: DataFetchingEnvironment) = personModellRepository.findById(env.getArgument("ident"))?.let {
        Person(
            Navn(
                fornavn = it.fornavn,
                mellomnavn = null,
                etternavn = it.etternavn,
                forkortetNavn = null,
                metadata = Metadata(false, "FREG")
            )
        )
    }
}
