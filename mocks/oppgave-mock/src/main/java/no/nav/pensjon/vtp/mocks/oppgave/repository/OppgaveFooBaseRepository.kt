package no.nav.pensjon.vtp.mocks.oppgave.repository

import java.util.*

interface OppgaveFooBaseRepository {
    fun findById(id: String): Optional<OppgaveFoo>
    fun findAll() : Collection<OppgaveFoo>
    fun save(oppgave: OppgaveFoo): OppgaveFoo
}
