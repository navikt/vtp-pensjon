package no.nav.pensjon.vtp.mocks.oppgave.repository

import org.springframework.data.repository.Repository

interface OppgaveFooRepository : Repository<OppgaveFoo, String> {
    fun findById(id: String): OppgaveFoo?
    fun findAll(): Collection<OppgaveFoo>
    fun save(oppgave: OppgaveFoo): OppgaveFoo
}
