package no.nav.pensjon.vtp.mocks.tp

import nav_cons_sto_sam_tjenestepensjon.no.nav.inf.*
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoFinnTjenestepensjonsforholdRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjon
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjonYtelse
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjonforhold
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.tjenestepensjon.FaultStoElementetErDuplikat
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.tjenestepensjon.FaultStoElementetHarOverlappendePeriode
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.tjenestepensjon.FaultStoTomDatoForanFomDato
import no.nav.lib.sto.sam.fault.FaultStoGenerisk
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.mocks.tss.SamhandlerRepository
import no.nav.pensjon.vtp.mocks.tss.tjenestepensjon
import no.nav.pensjon.vtp.mocks.tss.tpNr
import no.nav.pensjon.vtp.util.isOverlapping
import no.nav.pensjon.vtp.util.toLocalDate
import org.apache.commons.lang.time.DateUtils.isSameDay
import org.hibernate.validator.internal.util.CollectionHelper.asSet
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-tjenestepensjonWeb/sca/SAMTjenestepensjonWSEXP"])
@HandlerChain(file = "/Handler-chain.xml")
class SamTjenestepensjonMock(
    private val samhandlerRepository: SamhandlerRepository,
    private val tjenestepensjonRepository: TjenestepensjonRepository,
    private val tjenestepensjonService: SequenceService,
) : SAMTjenestepensjon {
    @WebMethod
    @RequestWrapper(
        localName = "opprettTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "opprettTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "opprettTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun opprettTjenestepensjonYtelse(
        @WebParam(
            name = "opprettTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) asboStoTjenestepensjon: ASBOStoTjenestepensjonforhold?
    ): ASBOStoTjenestepensjonYtelse {
        validateYtelseRequest(asboStoTjenestepensjon)
            .let { request ->
                if (request.tjenestepensjonYtelseListe.size != 1) {
                    throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("Request must contain one and only one ytelse, contained ${request.tjenestepensjonYtelseListe.size}", FaultStoGenerisk())
                }

                val ytelse: ASBOStoTjenestepensjonYtelse = validateYtelse(request.tjenestepensjonYtelseListe.first())

                if (ytelse.ytelseId != null) {
                    TODO("Update of Ytelse is not implemented")
                }

                val ytelseId = tjenestepensjonService.getNextVal("tjenestepensjonYtelse")

                val tjenestepensjon = tjenestepensjonRepository.findByForholdForholdId(request.forholdId)
                    ?: throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("No tjenestepensjon exists with forholdId=${request.forholdId}")

                val forhold = tjenestepensjon.forhold.first { it.forholdId == request.forholdId }

                if (forhold.ytelser.any {
                    it.ytelseKode == ytelse.ytelseKode &&
                        isSameDay(it.iverksattFom, ytelse.iverksattFom) &&
                        (it.iverksattTom != null && ytelse.iverksattTom != null && isSameDay(it.iverksattTom, ytelse.iverksattTom))
                }
                ) {
                    throw OpprettTjenestepensjonYtelseFaultStoElementetErDuplikatMsg("Forhold med forholdId=${request.forholdId} har allerede en tilsvarende ytelse av type=${ytelse.ytelseKode}", FaultStoElementetErDuplikat())
                }

                if (forhold.ytelser.any {
                    it.ytelseKode == ytelse.ytelseKode &&
                        isOverlapping(it.iverksattFom, it.iverksattTom, ytelse.iverksattFom, ytelse.iverksattTom)
                }
                ) {
                    throw OpprettTjenestepensjonYtelseFaultStoElementetHarOverlappendePeriodeMsg("Forhold med forholdId=${request.forholdId} har har en overlappenede ytelse av type=${ytelse.ytelseKode}", FaultStoElementetHarOverlappendePeriode())
                }

                val updateFordhold = forhold.copy(
                    endringsInfo = request.endringsInfo,
                    ytelser = forhold.ytelser union asSet(
                        Ytelse(
                            ytelseId = ytelseId.toString(),
                            innmeldtFom = ytelse.innmeldtFom,
                            ytelseKode = ytelse.ytelseKode,
                            ytelseBeskrivelse = ytelse.ytelseBeskrivelse,
                            iverksattFom = ytelse.iverksattFom,
                            iverksattTom = ytelse.iverksattTom,
                            endringsInfo = ytelse.endringsInfo,
                        )
                    )
                )

                tjenestepensjonRepository.save(
                    tjenestepensjon.copy(
                        endringsInfo = request.endringsInfo,
                        forhold = tjenestepensjon.forhold.toMutableSet().also {
                            it.remove(updateFordhold)
                            it.add(updateFordhold)
                        }
                    )
                )
                return ytelse.also {
                    it.ytelseId = ytelseId.toString()
                }
            }
    }

    private fun validateYtelse(ytelse: ASBOStoTjenestepensjonYtelse?): ASBOStoTjenestepensjonYtelse {
        if (ytelse == null) {
            throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("Ytelse was null", FaultStoGenerisk())
        } else if (ytelse.iverksattFom == null) {
            throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("Ytelse.iverksattFom was null", FaultStoGenerisk())
        } else if (ytelse.iverksattTom != null && ytelse.iverksattTom.toLocalDate().isBefore(ytelse.iverksattFom.toLocalDate())) {
            throw OpprettTjenestepensjonYtelseFaultStoTomDatoForanFomDatoMsg("Tom data foran fom dato", FaultStoTomDatoForanFomDato())
        } else {
            return ytelse
        }
    }

    private fun validateYtelseRequest(request: ASBOStoTjenestepensjonforhold?): ASBOStoTjenestepensjonforhold {
        if (request == null) {
            throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        } else if (request.forholdId == null) {
            throw OpprettTjenestepensjonYtelseFaultStoGeneriskMsg("Request.forholdId was null", FaultStoGenerisk())
        } else {
            return request
        }
    }

    @WebMethod
    @RequestWrapper(
        localName = "opprettTjenestepensjonsforhold",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonsforhold"
    )
    @ResponseWrapper(
        localName = "opprettTjenestepensjonsforholdResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonsforholdResponse"
    )
    @WebResult(name = "opprettTjenestepensjonsforholdResponse", targetNamespace = "")
    override fun opprettTjenestepensjonsforhold(
        @WebParam(
            name = "opprettTjenestepensjonsforholdRequest",
            targetNamespace = ""
        ) request: ASBOStoTjenestepensjon?
    ): ASBOStoTjenestepensjonforhold {
        request
            ?.let {
                if (request.tjenestepensjonsforholdListe.size != 1) {
                    throw OpprettTjenestepensjonsforholdFaultStoGeneriskMsg("Request must contain one and only one forhold, contained ${request.tjenestepensjonsforholdListe.size}")
                }

                val forhold = request.tjenestepensjonsforholdListe.first()

                if (forhold.harSimulering == true) {
                    throw OpprettTjenestepensjonsforholdFaultStoGeneriskMsg("Simulering is no longer supported by tp")
                }

                val forholdId = tjenestepensjonService.getNextVal("tjenestepensjonForhold")

                val tjenestepensjon = tjenestepensjonRepository.findById(request.fnr).orElse(null)
                    ?: Tjenestepensjon(pid = request.fnr)
                tjenestepensjon.endringsInfo = request.endringsInfo

                if (tjenestepensjon.forhold.any { it.tssEksternId == forhold.tssEksternId }) {
                    throw OpprettTjenestepensjonsforholdFaultStoElementetErDuplikatMsg("Person med fnr=${request.fnr} har allerede et for hold for tssEksternId=${forhold.tssEksternId}")
                }

                tjenestepensjonRepository.save(
                    tjenestepensjon.copy(
                        endringsInfo = request.endringsInfo,
                        forhold = tjenestepensjon.forhold union asSet(
                            Forhold(
                                forholdId = forholdId.toString(),
                                tssEksternId = forhold.tssEksternId,
                                navn = forhold.navn,
                                tpnr = forhold.tpnr,
                                harUtlandPensjon = forhold.harUtlandPensjon,
                                samtykkeSimuleringKode = forhold.samtykkeSimuleringKode,
                                samtykkeDato = forhold.samtykkeDato,
                                harSimulering = forhold.harSimulering,
                                tjenestepensjonSimulering = forhold.tjenestepensjonSimulering,
                                endringsInfo = forhold.endringsInfo,
                            )
                        )
                    )
                )
                return forhold.also {
                    it.forholdId = forholdId.toString()
                }
            }
            ?: throw OpprettTjenestepensjonsforholdFaultStoGeneriskMsg("Request was null")
    }

    @WebMethod
    @RequestWrapper(
        localName = "slettTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.SlettTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "slettTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.SlettTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "slettTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun slettTjenestepensjonYtelse(
        @WebParam(
            name = "slettTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) slettTjenestepensjonYtelseRequest: ASBOStoTjenestepensjonYtelse?
    ): String {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "finnTjenestepensjonsforhold",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforhold"
    )
    @ResponseWrapper(
        localName = "finnTjenestepensjonsforholdResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforholdResponse"
    )
    @WebResult(name = "finnTjenestepensjonsforholdResponse", targetNamespace = "")
    override fun finnTjenestepensjonsforhold(
        @WebParam(
            name = "finnTjenestepensjonsforholdRequest",
            targetNamespace = ""
        ) request: ASBOStoFinnTjenestepensjonsforholdRequest?
    ): ASBOStoTjenestepensjon =
        if (request != null) {
            with(request) {
                tjenestepensjonRepository.findById(fnr).orElse(null)
                    ?.let {
                        ASBOStoTjenestepensjon().apply {
                            fnr = it.pid
                            endringsInfo = it.endringsInfo
                            tjenestepensjonsforholdListe = it.forhold.map {
                                ASBOStoTjenestepensjonforhold().apply {
                                    val samhandler = if (hentSamhandlerInfo) {
                                        samhandlerRepository.findByTssEksternId(it.tssEksternId)
                                            ?: throw FinnTjenestepensjonsforholdFaultStoElementetFinnesIkkeMsg("Samhandler med tssEksternId=${it.tssEksternId} fantes ikke")
                                    } else {
                                        null
                                    }
                                    forholdId = it.forholdId.toString()
                                    tssEksternId = it.tssEksternId
                                    navn = it.navn
                                    tpnr = samhandler?.let { it.alternativeIder.tpNr() ?: it.offentligId }
                                    navn =
                                        samhandler?.let { it.avdelinger.tjenestepensjon().avdelingNavn ?: it.navn }
                                    harUtlandPensjon = it.harUtlandPensjon
                                    samtykkeSimuleringKode = it.samtykkeSimuleringKode
                                    samtykkeDato = it.samtykkeDato
                                    endringsInfo = it.endringsInfo

                                    // TP does no longer store or serve simulering
                                    harSimulering = false
                                    tjenestepensjonSimulering = null

                                    tjenestepensjonYtelseListe = it.ytelser
                                        // TODO: Filter
                                        .map {
                                            ASBOStoTjenestepensjonYtelse().apply {
                                                ytelseId = it.ytelseId
                                                innmeldtFom = it.innmeldtFom
                                                ytelseKode = it.ytelseKode
                                                ytelseBeskrivelse = it.ytelseBeskrivelse
                                                iverksattFom = it.iverksattFom
                                                iverksattTom = it.iverksattTom
                                                endringsInfo = it.endringsInfo
                                            }
                                        }.toTypedArray()
                                }
                            }.toTypedArray()
                        }
                    }
                    ?: ASBOStoTjenestepensjon().apply {
                        this.fnr = fnr
                        this.tjenestepensjonsforholdListe = emptyArray<ASBOStoTjenestepensjonforhold>()
                    }
            }
        } else {
            throw FinnTjenestepensjonsforholdFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        }

    @WebMethod
    @RequestWrapper(
        localName = "lagreTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.LagreTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "lagreTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.LagreTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "lagreTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun lagreTjenestepensjonYtelse(
        @WebParam(
            name = "lagreTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) lagreTjenestepensjonYtelseRequest: ASBOStoTjenestepensjonYtelse?
    ): ASBOStoTjenestepensjonYtelse {
        TODO("Not yet implemented")
    }
}
