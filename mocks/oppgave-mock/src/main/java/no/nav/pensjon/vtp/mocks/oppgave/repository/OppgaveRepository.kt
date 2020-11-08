package no.nav.pensjon.vtp.mocks.oppgave.repository

import no.nav.pensjon.vtp.core.util.toOptional
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class OppgaveRepository {
    private val oppgaver: MutableMap<String, OppgaveFoo> = ConcurrentHashMap()

    fun findById(id: String): Optional<OppgaveFoo> = oppgaver[id].toOptional()

    fun findAll() : Collection<OppgaveFoo> = oppgaver.values

    fun save(oppgave: OppgaveFoo): OppgaveFoo {
        oppgaver[oppgave.oppgaveId]
                ?.takeIf(oppgave::hasDifferentVersion)
                ?.let {
                    throw IllegalArgumentException(
                            "Oppgaven kan ikke oppdateres når gjeldende versjon og innsendt versjon ikke er like. " +
                                    "Gjeldende versjon: ${it.version}, angitt versjon: ${oppgave.version} uuid: ${oppgave.oppgaveId}"
                    )
                }

        oppgaver[oppgave.oppgaveId] = oppgave.withIncrementedVersion()
        return oppgave
    }
}
