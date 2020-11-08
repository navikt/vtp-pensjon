package no.nav.pensjon.vtp.mocks.oppgave.repository

import org.springframework.data.mongodb.repository.MongoRepository

interface OppgaveFooRepository : MongoRepository<OppgaveFoo, String>, OppgaveFooBaseRepository