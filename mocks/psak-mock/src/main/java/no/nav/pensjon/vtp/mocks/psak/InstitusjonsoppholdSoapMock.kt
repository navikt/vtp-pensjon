package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.virksomhet.grunnlag.institusjonsopphold.v1.ObjectFactory
import no.nav.virksomhet.tjenester.institusjonsopphold.meldinger.v1.HentInstitusjonsoppholdListeRequest
import no.nav.virksomhet.tjenester.institusjonsopphold.meldinger.v1.HentInstitusjonsoppholdListeResponse
import no.nav.virksomhet.tjenester.institusjonsopphold.v1.HentInstitusjonsoppholdListeFnrIkkeFunnet
import no.nav.virksomhet.tjenester.institusjonsopphold.v1.HentInstitusjonsoppholdListeSamhandlerIkkeFunnet
import no.nav.virksomhet.tjenester.institusjonsopphold.v1.Institusjonsopphold
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-institusjonsopphold_v1Web/sca/InstitusjonsoppholdWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/institusjonsopphold/v1", name = "Institusjonsopphold")
@XmlSeeAlso(ObjectFactory::class, no.nav.virksomhet.tjenester.felles.v1.ObjectFactory::class, no.nav.virksomhet.tjenester.institusjonsopphold.feil.v1.ObjectFactory::class, no.nav.virksomhet.tjenester.institusjonsopphold.meldinger.v1.ObjectFactory::class, no.nav.virksomhet.tjenester.institusjonsopphold.v1.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class InstitusjonsoppholdSoapMock : Institusjonsopphold {
    @WebMethod
    @RequestWrapper(localName = "hentInstitusjonsoppholdListe", targetNamespace = "http://nav.no/virksomhet/tjenester/institusjonsopphold/v1", className = "no.nav.virksomhet.tjenester.institusjonsopphold.v1.HentInstitusjonsoppholdListe")
    @ResponseWrapper(localName = "hentInstitusjonsoppholdListeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/institusjonsopphold/v1", className = "no.nav.virksomhet.tjenester.institusjonsopphold.v1.HentInstitusjonsoppholdListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Throws(HentInstitusjonsoppholdListeFnrIkkeFunnet::class, HentInstitusjonsoppholdListeSamhandlerIkkeFunnet::class)
    override fun hentInstitusjonsoppholdListe(hentInstitusjonsoppholdListeRequest: HentInstitusjonsoppholdListeRequest): HentInstitusjonsoppholdListeResponse {
        val opphold = no.nav.virksomhet.grunnlag.institusjonsopphold.v1.Institusjonsopphold()
        opphold.fnr = hentInstitusjonsoppholdListeRequest.fnr

        val response = HentInstitusjonsoppholdListeResponse()
        response.institusjonsopphold = opphold
        return response
    }
}
