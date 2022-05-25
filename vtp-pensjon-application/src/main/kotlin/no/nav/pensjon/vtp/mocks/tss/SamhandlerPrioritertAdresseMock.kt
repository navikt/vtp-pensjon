package no.nav.pensjon.vtp.mocks.tss

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.virksomhet.part.samhandler.v2.*
import no.nav.virksomhet.tjenester.samhandler.feil.v2.SamhandlerIkkeFunnet
import no.nav.virksomhet.tjenester.samhandler.meldinger.v2.*
import no.nav.virksomhet.tjenester.samhandler.v2.binding.*
import no.nav.virksomhet.tjenester.samhandler.v2.binding.Samhandler
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.*
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/samhandlerv2"])
@Addressing
@WebService(
    name = "Samhandler",
    targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2"
)
@XmlSeeAlso(
    no.nav.virksomhet.tjenester.samhandler.v2.ObjectFactory::class,
    no.nav.virksomhet.tjenester.samhandler.meldinger.v2.ObjectFactory::class,
    no.nav.virksomhet.tjenester.samhandler.feil.v2.ObjectFactory::class,
    no.nav.virksomhet.part.samhandler.v2.ObjectFactory::class,
)
@HandlerChain(file = "/Handler-chain.xml")
class SamhandlerPrioritertAdresseMock(
    private val samhandlerRepository: SamhandlerRepository
) : Samhandler {

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentAutorisasjonOgRettighetListe",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentAutorisasjonOgRettighetListe"
    )
    @ResponseWrapper(
        localName = "hentAutorisasjonOgRettighetListeResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentAutorisasjonOgRettighetListeResponse"
    )
    override fun hentAutorisasjonOgRettighetListe(p0: HentAutorisasjonOgRettighetListeRequest?): HentAutorisasjonOgRettighetListeResponse {
        TODO("Not yet implemented")
    }

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentSamhandlerNavn",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentSamhandlerNavn"
    )
    @ResponseWrapper(
        localName = "hentSamhandlerNavnResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentSamhandlerNavnResponse"
    )
    override fun hentSamhandlerNavn(p0: HentSamhandlerNavnRequest?): HentSamhandlerNavnResponse {
        TODO("Not yet implemented")
    }

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentSamhandlerPrioritertAdresse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentSamhandlerPrioritertAdresse"
    )
    @ResponseWrapper(
        localName = "hentSamhandlerPrioritertAdresseResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/samhandler/v2",
        className = "no.nav.virksomhet.tjenester.samhandler.v2.HentSamhandlerPrioritertAdresseResponse"
    )
    override fun hentSamhandlerPrioritertAdresse(
        @WebParam(name = "hentSamhandlerPrioritertAdresseRequest", targetNamespace = "") request: HentSamhandlerPrioritertAdresseRequest?
    ): HentSamhandlerPrioritertAdresseResponse {
        return if (request?.identKode == "TSS_EKSTERN_ID" && request.ident != null) {
            samhandlerRepository.findByTssEksternId(request.ident)
                ?.avdelinger
                ?.firstOrNull { it.idTSSEkstern == request.ident }
                ?.let {
                    HentSamhandlerPrioritertAdresseResponse().apply {
                        navn = it.avdelingNavn
                        postadresse = Adresse().apply {
                            adresselinje1 = it.pAdresse.adresselinje1
                            adresselinje2 = it.pAdresse.adresselinje2
                            adresselinje3 = it.pAdresse.adresselinje3
                            land = it.pAdresse.land?.let { Land().apply { kode =  it} }
                            postnr = it.pAdresse.postNr
                            poststed = it.pAdresse.poststed
                            kommunenr = it.pAdresse.kommuneNr
                        }
                    }
                }
                ?: throw HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet("Samhandler ikke funnet: ${request.ident}", SamhandlerIkkeFunnet())
        } else {
            throw HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet("Samhandler identKode er ulik TSS_EKSTERN_ID eller ident mangler", SamhandlerIkkeFunnet())
        }
    }
}