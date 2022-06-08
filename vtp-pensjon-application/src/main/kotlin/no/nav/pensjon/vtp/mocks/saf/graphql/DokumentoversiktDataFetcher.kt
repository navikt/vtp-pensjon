package no.nav.pensjon.vtp.mocks.saf.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.mocks.saf.Dokumentoversikt
import no.nav.pensjon.vtp.mocks.saf.SideInfo
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository

class DokumentoversiktDataFetcher(private val journalpostRepository: JournalRepository) :
    DataFetcher<Dokumentoversikt> {
    override fun get(env: DataFetchingEnvironment): Dokumentoversikt =
        with(journalpostRepository.finnJournalposterMedFnr(env.getArgument("fagsakId"))) {
            Dokumentoversikt(
                this.map { t -> JournalpostBuilder.buildFrom(t) }.toList(),
                SideInfo("sluttpeker", false)
            )
        }
}