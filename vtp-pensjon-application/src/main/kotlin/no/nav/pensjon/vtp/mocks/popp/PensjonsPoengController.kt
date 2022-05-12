package no.nav.pensjon.vtp.mocks.popp

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*
import kotlin.random.Random

@RestController
@Tag(name = "POPP Pensjonspoeng")
@RequestMapping("/rest/popp/api/")
class PensjonsPoengController { // http://localhost:8060/rest/popp/api/pensjonspoeng"

    @GetMapping(path = ["/pensjonspoeng"])
    fun hentPensjonspoengListe(
        @RequestHeader("fnr") fodselsnummer: String?,
        @RequestParam(required = false) fomAr: Int?,
        @RequestParam(required = false) tomAr: Int?,
        @RequestParam(required = false) pensjonspoengType: String?
    ): HentPensjonspoengListeResponse {
        val fnr = Pid(fodselsnummer)
        return HentPensjonspoengListeResponse().apply {

            fomAr?.let { fomAr ->
                tomAr?.let { tomAr ->
                    IntRange(fomAr, tomAr)
                        .forEach {
                            pensjonspoeng.add(
                                Pensjonspoeng().apply {
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

    @GetMapping(path = ["/opptjeningsgrunnlag/{fnr}"])
    fun hentOpptjeningsGrunnlag(@PathVariable fnr: String, @RequestParam grunnlagsTypeListe: List<String>): HentOpptjeningsGrunnlagResponse {
        return HentOpptjeningsGrunnlagResponse().apply {
            opptjeningsGrunnlag = OpptjeningsGrunnlag().apply {
                this.fnr = Pid(fnr)
            }
        }
    }

    class Pid(val pid: String?) : Serializable

    class HentPensjonspoengListeResponse : Serializable {
        val pensjonspoeng: MutableList<Pensjonspoeng> = mutableListOf()
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

    class Pensjonspoeng : Serializable {
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

    class HentOpptjeningsGrunnlagResponse : Serializable {
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
