package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository

data class Person(
    val navn: Navn
)

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Person> {

    override fun get(env: DataFetchingEnvironment): Person? {
        val personModell = personModellRepository.findById(env.getArgument("ident")) ?: return null
        return Person(
            Navn(
                fornavn = personModell.fornavn,
                mellomnavn = null,
                etternavn = personModell.etternavn,
                forkortetNavn = null,
                metadata = Metadata(false, "FREG")
            )
        )
    }
}
