package no.nav.pensjon.vtp.mocks.popp

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*
import kotlin.random.Random

@RestController
@Tag(name = "POPP Pensjonspoeng, opptjeningsrunnlag og beholdning")
@RequestMapping("/rest")
class POPPMock {

    @GetMapping(value = ["/popp/api/pensjonspoeng"])
    fun hentPensjonspoeng(
        @RequestHeader("fnr", required = true) fodselsnummer: String,
        @RequestParam("fomAr", required = false) fomAr: Int?,
        @RequestParam("tomAr", required = false) tomAr: Int?,
        @RequestParam("pensjonspoengType", required = false) pensjonspoengType: String?
    ): PensjonspoengListeResponse {
        val fnr = Pid(fodselsnummer)
        return PensjonspoengListeResponse().apply {

            fomAr?.let { fomAr ->
                tomAr?.let { tomAr ->
                    IntRange(fomAr, tomAr)
                        .forEach {
                            pensjonspoeng.add(
                                Poeng().apply {
                                    pensjonspoengId = Random.nextLong()
                                    this.fnr = fnr
                                    fnrOmsorgFor = null
                                    kilde = "k"
                                    this.pensjonspoengType = pensjonspoengType
                                    inntekt = Inntekt().apply {
                                        inntektId = Random.nextLong()
                                        this.fnr = fnr
                                        kilde = "k"
                                        kommune = "Oslo"
                                        piMerke = "pim"
                                        inntektAr = it
                                        belop = Random.nextLong(400000, 600000)
                                        changeStamp = ChangeStamp(Date(), "vtp", Date(), "vtp")
                                        inntektType = "SUM_PI" // Sum pensjonsgivende inntekt
                                    }
                                    omsorg = null
                                    ar = it
                                    anvendtPi = 1
                                    poeng = 20.0
                                    maxUforegrad = 70
                                    changeStamp = ChangeStamp(Date(), "vtp", Date(), "vtp")
                                }
                            )
                        }
                }
            }
        }
    }

    @GetMapping(value = ["/popp/api/opptjeningsgrunnlag/{fnr}"])
    fun hentOpptjeningsGrunnlag(@PathVariable fnr: String, @RequestParam(required = false) grunnlagsTypeListe: List<String>? = null): OpptjeningsGrunnlagResponse {
        return OpptjeningsGrunnlagResponse().apply {
            opptjeningsGrunnlag = OpptjeningsGrunnlag().apply {
                this.fnr = Pid(fnr)
            }
        }
    }

    @PostMapping(value = ["/popp/api/beholdning"])
    fun hentBeholdningListe(@RequestBody(required = false) body: BeholdningListeRequest): BeholdningListeResponse {
        return BeholdningListeResponse()
    }

    class BeholdningListeResponse {
        val beholdninger = emptyList<String>()
    }

    class BeholdningListeRequest {
        private val fnr: Pid? = null
        private val beholdningType: String? = null
        private val serviceDirectiveTPOPP006: String? = null
        private val fomDato: Date? = null
        private val tomDato: Date? = null
    }

    class Pid(val pid: String?) : Serializable

    class PensjonspoengListeResponse : Serializable {
        val pensjonspoeng: MutableList<Poeng> = mutableListOf()
    }

    class Inntekt : Serializable {
        var inntektId: Long? = null
        var fnr: Pid? = null
        var kilde: String? = null
        var kommune: String? = null
        var piMerke: String? = null
        var inntektAr: Int? = null
        var belop: Long? = null
        var changeStamp: ChangeStamp? = null
        var inntektType: String? = null
    }

    class Poeng : Serializable {
        var pensjonspoengId: Long? = null
        var fnr: Pid? = null
        var fnrOmsorgFor: Pid? = null
        var kilde: String? = null
        var pensjonspoengType: String? = null
        var inntekt: Inntekt? = null
        var omsorg: String? = null
        var ar: Int? = null
        var anvendtPi: Int? = null
        var poeng: Double? = null
        var maxUforegrad: Int? = null
        var changeStamp: ChangeStamp? = null
    }

    class OpptjeningsGrunnlagResponse : Serializable {
        var opptjeningsGrunnlag: OpptjeningsGrunnlag? = null
    }

    class OpptjeningsGrunnlag : Serializable {
        var fnr: Pid? = null
        var inntektListe: List<Inntekt>? = emptyList()
        var omsorgListe: List<String>? = emptyList()
        var dagpengerListe: List<String>? = emptyList()
        var forstegangstjeneste: String? = null
    }
}
