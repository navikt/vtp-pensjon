package no.nav.pensjon.vtp.mocks.tp

import nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforholdFaultStoElementetFinnesIkkeMsg
import no.nav.pensjon.vtp.mocks.tss.SamhandlerRepository
import no.nav.pensjon.vtp.mocks.tss.tjenestepensjon
import no.nav.pensjon.vtp.mocks.tss.tpNr
import no.nav.pensjon.vtp.util.asGregorianCalendar
import org.springframework.stereotype.Service

@Service
class TjenestepensjonService(
    private val sequenceService: SequenceService,
    private val samhandlerRepository: SamhandlerRepository,
    private val tjenestepensjonRepository: TjenestepensjonRepository,
) {
    fun save(pid: String, tjenestepensjon: no.nav.pensjon.vtp.testmodell.scenario.sam.Tjenestepensjon): Tjenestepensjon {
        return tjenestepensjonRepository.save(
            Tjenestepensjon(
                pid = pid,
                forhold = tjenestepensjon.forhold.map { forhold ->
                    val samhandler = samhandlerRepository.findByTpNr(forhold.tpNr!!) ?: throw IllegalArgumentException("Unknown samhandler with tpNr=${forhold.tpNr}")
                    val tssEksternId = samhandler.avdelinger.tjenestepensjon().idTSSEkstern

                    Forhold(
                        forholdId = forholdIdNextVal(),
                        tssEksternId = tssEksternId,
                        tpnr = getTpNrByTssEksternId(tssEksternId),
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

    fun getTpNrByTssEksternId(tssEksternId: String) = samhandlerRepository.findByTssEksternId(tssEksternId)
            ?.let { it.alternativeIder.tpNr() ?: it.offentligId }
            ?: throw FinnTjenestepensjonsforholdFaultStoElementetFinnesIkkeMsg("Samhandler med tssEksternId=$tssEksternId fantes ikke")
}
