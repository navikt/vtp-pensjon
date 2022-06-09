package no.nav.pensjon.vtp.mocks.saf.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.mocks.saf.Journalpost
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository

class JournalpostDataFetcher(private val journalpostRepository: JournalRepository) : DataFetcher<Journalpost> {
    override fun get(env: DataFetchingEnvironment): Journalpost? =
        with(journalpostRepository.finnJournalpostMedJournalpostId(env.getArgument("journalpostId"))) {
            if (this == null) return@with null
            return JournalpostBuilder.buildFrom(this)
        }
}

