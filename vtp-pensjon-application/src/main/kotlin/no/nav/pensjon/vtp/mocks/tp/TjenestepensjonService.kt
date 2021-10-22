package no.nav.pensjon.vtp.mocks.tp

import no.nav.pensjon.vtp.mocks.tss.SamhandlerRepository
import no.nav.pensjon.vtp.mocks.tss.tjenestepensjon
import no.nav.pensjon.vtp.util.asGregorianCalendar
import org.springframework.stereotype.Service

@Service
class TjenestepensjonService(
    private val sequenceService: SequenceService,
    private val samhandlerRepository: SamhandlerRepository,
    private val tjenestepensjonRepository: TjenestepensjonRepository,
) {
    fun save(pid: String, tjenestepensjon: no.nav.pensjon.vtp.testmodell.scenario.pensjon.Tjenestepensjon): Tjenestepensjon {
        return tjenestepensjonRepository.save(
            Tjenestepensjon(
                pid = pid,
                forhold = tjenestepensjon.forhold.map { forhold ->
                    val samhandler = samhandlerRepository.findByTpNr(forhold.tpNr!!) ?: throw IllegalArgumentException("Unknown samhandler with tpNr=${forhold.tpNr}")

                    Forhold(
                        forholdId = forholdIdNextVal(),
                        tssEksternId = samhandler.avdelinger.tjenestepensjon().idTSSEkstern,
                        tpnr = forhold.tpNr,
                        ytelser = forhold.ytelser.map { ytelse ->
                            Ytelse(
                                ytelseId = ytelseIdIdNextVal(),
                                innmeldtFom = ytelse.innmeldtFom.asGregorianCalendar(),
                                ytelseKode = ytelse.type,
                                iverksattFom = ytelse.iverksattFom?.asGregorianCalendar(),
                                iverksattTom = ytelse.iverksattTom?.asGregorianCalendar(),
                            )
                        }.toSet()
                    )
                }.toSet()
            )
        )
    }

    fun findByPid(pid: String): Tjenestepensjon? {
        return tjenestepensjonRepository.findByPid(pid)
    }

    fun forholdIdNextVal() = sequenceService.getNextVal("tjenestepensjonForhold").toString()

    fun ytelseIdIdNextVal() = sequenceService.getNextVal("tjenestepensjonYtelse").toString()
}
