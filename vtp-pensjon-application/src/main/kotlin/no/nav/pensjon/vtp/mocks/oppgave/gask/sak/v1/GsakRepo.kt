package no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.tjeneste.virksomhet.sak.v1.binding.HentSakSakIkkeFunnet
import no.nav.tjeneste.virksomhet.sak.v1.feil.SakIkkeFunnet
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.*
import org.springframework.stereotype.Component
import java.time.LocalDate.now
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Component
class GsakRepo {
    private val bySakId: MutableMap<String, Sak>
    private val sakIder: AtomicInteger
    private val oppgaveIder: AtomicInteger

    fun hentSak(sakId: String) = bySakId[sakId]
        ?: throw HentSakSakIkkeFunnet("Finner ikke sak $sakId", SakIkkeFunnet())

    val alleSaker: Collection<Sak>
        get() = bySakId.values

    fun leggTilSak(personer: List<PersonModell>, fagomrade: String?, fagsystem: String?, saktype: String?): Sak {
        val sak = Sak().apply {
            sakId = nextSakId()
            endretAv = SAKSBEHANDLER_IDENT

            fagomraade = Fagomraader()
                .apply {
                    value = fagomrade
                }

            this.sakstype = Sakstyper().apply {
                value = saktype
            }

            this.fagsystem = Fagsystemer().apply {
                value = fagsystem
            }

            endringstidspunkt = now().minusDays(10).asXMLGregorianCalendar()
            opprettelsetidspunkt = now().minusDays(10).minusDays(10).asXMLGregorianCalendar()
            opprettelsetidspunkt = now().minusDays(10).plusDays(5).asXMLGregorianCalendar()
            opprettetAv = SAKSBEHANDLER_IDENT
            versjonsnummer = "1"

            gjelderBrukerListe.addAll(
                personer
                    .map {
                        Person().apply {
                            ident = it.aktørIdent
                        }
                    }
            )
        }

        bySakId[sak.sakId] = sak
        return sak
    }

    private fun nextSakId() = sakIder.incrementAndGet().toString()

    fun opprettOppgave(sakId: String): String {
        return sakId + oppgaveIder.incrementAndGet()
    }

    companion object {
        private const val SAKSBEHANDLER_IDENT = "MinSaksbehandler"
    }

    init {
        val formatter = DateTimeFormatter.ofPattern("Mdkm")
        sakIder = AtomicInteger(LocalDateTime.now().format(formatter).toInt() * 100)
        oppgaveIder = AtomicInteger(LocalDateTime.now().format(formatter).toInt() * 100)
        bySakId = HashMap()
    }
}
