package no.nav.pensjon.vtp.mocks.oppgave.repository

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class OppgaveRepository {
    private val oppgaver: MutableMap<String, OppgaveFoo> = ConcurrentHashMap()

    fun findById(id: String): OppgaveFoo? = oppgaver[id]

    fun findAll() : Collection<OppgaveFoo> = oppgaver.values

    fun save(oppgave: OppgaveFoo): String {
        oppgaver[oppgave.oppgaveId]
                ?.takeIf(oppgave::hasDifferentVersion)
                ?.let {
                    throw IllegalArgumentException(
                            "Oppgaven kan ikke oppdateres når gjeldende versjon og innsendt versjon ikke er like. " +
                                    "Gjeldende versjon: ${it.version}, angitt versjon: ${oppgave.version} uuid: ${oppgave.oppgaveId}"
                    )
                }

        oppgaver[oppgave.oppgaveId] = oppgave.withIncrementedVersion()
        return oppgave.oppgaveId
    }
}
